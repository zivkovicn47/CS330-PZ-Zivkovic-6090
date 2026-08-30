package com.zivkovic.project250.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.zivkovic.project250.domain.CategoryModel
import androidx.compose.runtime.State
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CategoryViewModel: ViewModel() {
    private val _categories = mutableStateOf<List<CategoryModel>>(emptyList())
    val categories: State<List<CategoryModel>> = _categories

    private val _isLoading=mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    init {
        loadCategories()
    }
    private fun loadCategories() {
        val ref = FirebaseDatabase.getInstance("https://project250-65f0d-default-rtdb.europe-west1.firebasedatabase.app").getReference("Category")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<CategoryModel>()
                for (child in snapshot.children) {
                    val model = child.getValue(CategoryModel::class.java)
                    if (model != null) {
                        list.add(model)
                    }
                }
                _categories.value = list
                _isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                _isLoading.value = false
            }
        })
    }
}