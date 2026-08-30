package com.zivkovic.project250.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.zivkovic.project250.domain.CarModel

class CarViewModel : ViewModel() {

    companion object {
        const val DB_URL =
            "https://project250-65f0d-default-rtdb.europe-west1.firebasedatabase.app"
    }

    private fun carsRef(): DatabaseReference =
        FirebaseDatabase.getInstance(DB_URL).getReference("Cars")

    private val _searchText = mutableStateOf("")
    val searchText: State<String> = _searchText

    private val _cars = mutableStateOf<List<CarModel>>(emptyList())
    var cars: State<List<CarModel>> = _cars // Keeping original list exposed if needed, but UI should primarily use filtered

    private val _selectedCategoryId = mutableStateOf<Int?>(null)
    val selectedCategoryId: State<Int?> = _selectedCategoryId

    // Computed properties for filtered lists
    val filteredCars: List<CarModel>
        get() {
            var result = _cars.value

            // Filter by search text
            if (_searchText.value.isNotBlank()) {
                result = result.filter { it.title.contains(_searchText.value, ignoreCase = true) }
            }

            // Filter by category
            if (_selectedCategoryId.value != null) {
                result = result.filter { it.categoryIdInt == _selectedCategoryId.value }
            }

            return result
        }

    val filteredFavoriteCars: List<CarModel>
        get() {
            var result = _cars.value.filter { _favoriteIds.value.contains(it.id) }

            // Filter by search text
            if (_searchText.value.isNotBlank()) {
                result = result.filter { it.title.contains(_searchText.value, ignoreCase = true) }
            }

            // Filter by category
            if (_selectedCategoryId.value != null) {
                result = result.filter { it.categoryIdInt == _selectedCategoryId.value }
            }

            return result
        }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onCategorySelected(id: Int) {
        if (_selectedCategoryId.value == id) {
            _selectedCategoryId.value = null // Toggle off
        } else {
            _selectedCategoryId.value = id
        }
    }

    private val _isLoading = mutableStateOf(value = true)
    val isLoading: State<Boolean> = _isLoading

    private val _favoriteIds = mutableStateOf<Set<String>>(emptySet())
    val favoriteIds: State<Set<String>> = _favoriteIds

    // Aktivni listeneri. Reference cuvamo da bismo ih odjavili u onCleared() i da
    // ponovljeni poziv fetch* funkcija ne bi zakacio isti listener vise puta.
    private var carsListener: ValueEventListener? = null
    private var userCarsQuery: Query? = null
    private var userCarsListener: ValueEventListener? = null
    private var userCarsUid: String? = null
    private var favoritesRef: DatabaseReference? = null
    private var favoritesListener: ValueEventListener? = null

    init {
        fetchCars()
        fetchFavorites()
    }

    private fun DataSnapshot.toCarList(): List<CarModel> {
        val temp = mutableListOf<CarModel>()
        for (child in children) {
            val car = child.getValue(CarModel::class.java)
            car?.let {
                it.id = child.key ?: ""
                temp.add(it)
            }
        }
        return temp
    }

    /**
     * Trajna pretplata na cvor Cars. Izmena napravljena na web strani stize u
     * aplikaciju bez rucnog osvezavanja (ugovor o sinhronizaciji, tacka 11).
     */
    fun fetchCars() {
        if (carsListener != null) return // vec smo pretplaceni
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _cars.value = snapshot.toCarList()
                _isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                _isLoading.value = false
            }
        }
        carsListener = listener
        carsRef().addValueEventListener(listener)
    }

    private fun fetchFavorites() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "test_user_default"
        val ref = FirebaseDatabase.getInstance(DB_URL)
            .getReference("Users").child(userId).child("favorites")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val favs = mutableSetOf<String>()
                for (child in snapshot.children) {
                    child.key?.let { favs.add(it) }
                }
                _favoriteIds.value = favs
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        favoritesRef = ref
        favoritesListener = listener
        ref.addValueEventListener(listener)
    }

    fun toggleFavorite(carId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "test_user_default"
        val ref = FirebaseDatabase.getInstance(DB_URL)
            .getReference("Users").child(userId).child("favorites")

        if (_favoriteIds.value.contains(carId)) {
            ref.child(carId).removeValue()
        } else {
            ref.child(carId).setValue(true)
        }
    }

    private val _userCars = mutableStateOf<List<CarModel>>(emptyList())
    val userCars: State<List<CarModel>> = _userCars

    fun fetchUserCars() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            detachUserCarsListener()
            _userCars.value = emptyList()
            return
        }
        if (userCarsListener != null && userCarsUid == userId) return // vec smo pretplaceni

        detachUserCarsListener()

        val query = carsRef().orderByChild("userId").equalTo(userId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _userCars.value = snapshot.toCarList()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        }
        userCarsQuery = query
        userCarsListener = listener
        userCarsUid = userId
        query.addValueEventListener(listener)
    }

    private fun detachUserCarsListener() {
        userCarsListener?.let { listener -> userCarsQuery?.removeEventListener(listener) }
        userCarsQuery = null
        userCarsListener = null
        userCarsUid = null
    }

    fun addCar(car: CarModel, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            onError("User not logged in")
            return
        }
        val ref = carsRef()
        val newKey = ref.push().key
        if (newKey != null) {
            car.id = newKey
            // userId je uvek stvarni UID prijavljenog korisnika (tacka 4).
            val carWithUser = car.copy(userId = userId)

            // Kljuc se ne upisuje u telo zapisa: polje id je oznaceno sa
            // @get:Exclude pa ga Firebase ne serijalizuje (tacka 3.3).
            ref.child(newKey).setValue(carWithUser)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { onError(it.message ?: "Unknown error") }
        } else {
            onError("Failed to generate key")
        }
    }

    fun deleteCar(carId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        carsRef().child(carId).removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Unknown error") }
    }

    /**
     * Izmena ide preko updateChildren, ne setValue (ugovor o sinhronizaciji, tacka 3.2).
     * setValue bi obrisao svako polje koje ova aplikacija ne poznaje, konkretno
     * phone koje upisuje web strana. userId se namerno ne dira: izmenom oglasa
     * vlasnik se ne menja.
     */
    fun updateCar(carId: String, updatedCar: CarModel, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            onError("User not logged in")
            return
        }

        // Numericka polja se salju kao brojevi (tacka 3.1).
        val updates = mapOf(
            "title" to updatedCar.title,
            "price" to updatedCar.priceInt,
            "description" to updatedCar.description,
            "picUrl" to updatedCar.picUrl,
            "categoryId" to updatedCar.categoryIdInt,
            "productionYear" to updatedCar.productionYearInt,
            "mileage" to updatedCar.mileageInt,
            "fuelType" to updatedCar.fuelType,
            "transmission" to updatedCar.transmission,
            "engineVolume" to updatedCar.engineVolumeInt,
            "enginePower" to updatedCar.enginePowerInt,
            "highestSpeed" to updatedCar.highestSpeedInt,
            "seats" to updatedCar.seatsInt,
            // null brise cvor: korisnik je ispraznio polje za telefon
            "phone" to updatedCar.phone?.takeIf { it.isNotBlank() }
        )

        carsRef().child(carId).updateChildren(updates)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Unknown error") }
    }

    override fun onCleared() {
        super.onCleared()
        carsListener?.let { carsRef().removeEventListener(it) }
        carsListener = null
        detachUserCarsListener()
        favoritesListener?.let { listener -> favoritesRef?.removeEventListener(listener) }
        favoritesRef = null
        favoritesListener = null
    }
}
