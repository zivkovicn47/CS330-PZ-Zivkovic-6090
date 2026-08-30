package com.zivkovic.project250.ui.feature.addcar

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.zivkovic.project250.domain.CarModel
import com.zivkovic.project250.viewModel.CarViewModel
import com.zivkovic.project250.viewModel.CategoryViewModel
import androidx.compose.material.icons.filled.ArrowBack
import com.zivkovic.project250.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCarScreen(
    carId: String? = null,
    onBack: () -> Unit,
    carViewModel: CarViewModel,
    categoryViewModel: CategoryViewModel
) {
    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var engine by remember { mutableStateOf("") }
    var speed by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var mileage by remember { mutableStateOf("") }
    var fuel by remember { mutableStateOf("") }
    var transmission by remember { mutableStateOf("") }
    var volume by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Int?>(null) }

    // Dropdown States
    var expandedFuel by remember { mutableStateOf(false) }
    var expandedTransmission by remember { mutableStateOf(false) }
    // Vrednosti moraju biti identicne web strani, karakter po karakter
    // (ugovor o sinhronizaciji, tacka 5).
    val fuelOptions = listOf("Benzin", "Dizel", "Električni", "Hibrid", "Plin")
    val transmissionOptions = listOf("Manuelni", "Automatski")
    
    val context = LocalContext.current
    val categories by categoryViewModel.categories
    // We need to access the list of cars to find the one to edit
    val cars by carViewModel.cars 

    // Lista se sada osvezava uzivo, pa formu popunjavamo tek kad trazeni oglas
    // stigne - i samo jednom, da osvezavanje ne pregazi ono sto korisnik kuca.
    var prefilled by remember(carId) { mutableStateOf(false) }

    LaunchedEffect(carId, cars) {
        if (carId != null && !prefilled) {
            val carToEdit = cars.find { it.id == carId }
            carToEdit?.let {
                prefilled = true
                title = it.title
                price = it.priceInt.toString()
                description = it.description
                imageUrl = it.picUrl
                engine = it.enginePowerInt.toString()
                speed = it.highestSpeedInt.toString()
                capacity = it.seatsInt.toString()
                selectedCategoryId = it.categoryIdInt
                
                // New fields
                year = it.productionYearInt.toString()
                mileage = it.mileageInt.toString()
                fuel = it.fuelType
                transmission = it.transmission
                volume = it.engineVolumeInt.toString()
                phone = it.phone ?: ""
            }
        }
    }

    val isEditMode = carId != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text(
                if (isEditMode) "Edit Car" else "Add New Car",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        // Basic Info
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Car Title (e.g. BMW M5)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(8.dp))
        
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price ($)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        Spacer(Modifier.height(8.dp))
        
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
        Spacer(Modifier.height(8.dp))

        // Image URL
        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("Paste Image Link") },
            placeholder = { Text("https://example.com/car.jpg") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Text(
            "Copy an image address from Google and paste it here.",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
        )
        
        Spacer(Modifier.height(16.dp))

        // Specs
        Text("Specifications", fontWeight = FontWeight.SemiBold)
        // Row 1: Engine, Speed, Seats
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = engine,
                onValueChange = { engine = it },
                label = { Text("Engine (HP)") }, 
                placeholder = { Text("500") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = speed,
                onValueChange = { speed = it },
                label = { Text("Speed (km/h)") }, 
                placeholder = { Text("300") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = capacity,
                onValueChange = { capacity = it },
                label = { Text("Seats") }, 
                placeholder = { Text("4") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Spacer(Modifier.height(8.dp))
        
        // Row 2: Year, Mileage, Volume
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
             OutlinedTextField(
                value = year,
                onValueChange = { year = it },
                label = { Text("Year") }, 
                placeholder = { Text("2023") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
             OutlinedTextField(
                value = mileage,
                onValueChange = { mileage = it },
                label = { Text("Mileage") }, 
                placeholder = { Text("10000") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
             OutlinedTextField(
                value = volume,
                onValueChange = { volume = it },
                label = { Text("Volume (cc)") }, 
                placeholder = { Text("3000") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Spacer(Modifier.height(8.dp))
        
        // Row 3: Fuel, Transmission
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Fuel Dropdown
            Box(Modifier.weight(1f)) {
                ExposedDropdownMenuBox(
                    expanded = expandedFuel,
                    onExpandedChange = { expandedFuel = !expandedFuel }
                ) {
                    OutlinedTextField(
                        value = fuel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Fuel Type") },
                        placeholder = { Text("Select") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFuel) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedFuel,
                        onDismissRequest = { expandedFuel = false }
                    ) {
                        fuelOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    fuel = option
                                    expandedFuel = false
                                }
                            )
                        }
                    }
                }
            }

            // Transmission Dropdown
            Box(Modifier.weight(1f)) {
                ExposedDropdownMenuBox(
                    expanded = expandedTransmission,
                    onExpandedChange = { expandedTransmission = !expandedTransmission }
                ) {
                    OutlinedTextField(
                        value = transmission,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Transmission") },
                        placeholder = { Text("Select") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTransmission) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedTransmission,
                        onDismissRequest = { expandedTransmission = false }
                    ) {
                        transmissionOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    transmission = option
                                    expandedTransmission = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Contact (optional) - polje phone iz ugovora o sinhronizaciji, tacka 6
        OutlinedTextField(
            value = phone,
            onValueChange = { if (it.length <= 30) phone = it },
            label = { Text("Contact Phone (optional)") },
            placeholder = { Text("+381 60 123 4567") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        // Category Selector
        Text("Select Category", fontWeight = FontWeight.SemiBold)
        LazyRow(
            contentPadding = PaddingValues(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                val isSelected = category.idInt == selectedCategoryId
                val borderColor = if (isSelected) Color.Black else Color.LightGray
                val borderWidth = if (isSelected) 2.dp else 1.dp
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(borderWidth, borderColor, CircleShape)
                            .clickable { selectedCategoryId = category.idInt },
                        contentAlignment = Alignment.Center
                    ) {
                         AsyncImage(
                             model = coil.request.ImageRequest.Builder(LocalContext.current)
                                 .data(category.picUrl)
                                 .crossfade(true)
                                 .error(R.drawable.car)
                                 .fallback(R.drawable.car)
                                 .build(),
                             contentDescription = null,
                             modifier = Modifier.size(40.dp).padding(8.dp),
                             contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                             placeholder = androidx.compose.ui.res.painterResource(R.drawable.car),
                             error = androidx.compose.ui.res.painterResource(R.drawable.car)
                         )
                    }
                    Text(
                        text = category.title,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Action Button
        // Action Button
        Button(
            onClick = {
                // Svi brojevi se parsiraju pre provere - u bazu nikad ne ide string
                // u numerickom polju (ugovor o sinhronizaciji, tacka 3.1).
                val priceVal = price.toIntOrNull() ?: 0
                val engineVal = engine.toIntOrNull() ?: 0
                val speedVal = speed.toIntOrNull() ?: 0
                val seatsVal = capacity.toIntOrNull() ?: 0
                val yearVal = year.toIntOrNull() ?: 0
                val mileageVal = mileage.toIntOrNull() ?: -1
                val volumeVal = volume.toIntOrNull() ?: -1

                // Uslovi odgovaraju sigurnosnim pravilima baze (tacka 9) i
                // zabrani praznih enumeracija (tacka 3.4).
                val error = when {
                    title.trim().length !in 3..80 ->
                        "Title must be between 3 and 80 characters"
                    priceVal <= 0 -> "Price must be a number greater than 0"
                    description.length > 1000 ->
                        "Description must be at most 1000 characters"
                    !imageUrl.trim().startsWith("http://") &&
                        !imageUrl.trim().startsWith("https://") ->
                        "Image link must start with http:// or https://"
                    selectedCategoryId == null -> "Please select a category"
                    yearVal !in 1950..2100 -> "Year must be between 1950 and 2100"
                    mileageVal < 0 -> "Mileage must be 0 or more"
                    fuel !in fuelOptions -> "Please select a fuel type"
                    transmission !in transmissionOptions -> "Please select a transmission"
                    volumeVal < 0 -> "Engine volume must be 0 or more"
                    engineVal <= 0 -> "Engine power must be greater than 0"
                    speedVal <= 0 -> "Max speed must be greater than 0"
                    seatsVal !in 1..9 -> "Seats must be between 1 and 9"
                    phone.trim().length > 30 -> "Phone must be at most 30 characters"
                    else -> null
                }

                if (error != null) {
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                    return@Button
                }

                try {
                    val carData = CarModel(
                        title = title.trim(),
                        price = priceVal,
                        description = description,
                        picUrl = imageUrl.trim(),
                        enginePower = engineVal,
                        highestSpeed = speedVal,
                        seats = seatsVal,
                        categoryId = selectedCategoryId!!,
                        productionYear = yearVal,
                        mileage = mileageVal,
                        fuelType = fuel,
                        transmission = transmission,
                        engineVolume = volumeVal,
                        phone = phone.trim().ifBlank { null }
                    )

                    if (isEditMode) {
                        carViewModel.updateCar(
                            carId = carId!!,
                            updatedCar = carData,
                            onSuccess = {
                                Toast.makeText(context, "Car Updated Successfully!", Toast.LENGTH_SHORT).show()
                                onBack()
                            },
                            onError = {
                                Toast.makeText(context, "Error: $it", Toast.LENGTH_LONG).show()
                            }
                        )
                    } else {
                        carViewModel.addCar(
                            car = carData,
                            onSuccess = {
                                Toast.makeText(context, "Car Added Successfully!", Toast.LENGTH_SHORT).show()
                                onBack()
                            },
                            onError = {
                                Toast.makeText(context, "Error: $it", Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(context, "Invalid number format", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(
                if (isEditMode) "SAVE CHANGES" else "ADD CAR",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(Modifier.height(32.dp))
    }
}
