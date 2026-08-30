package com.zivkovic.project250.ui.feature.managecars

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.zivkovic.project250.domain.CarModel
import com.zivkovic.project250.viewModel.CarViewModel
import com.zivkovic.project250.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCarsScreen(
    onBack: () -> Unit,
    onEditCar: (String) -> Unit,
    carViewModel: CarViewModel,
    isAdmin: Boolean = false
) {
    val userCars by carViewModel.userCars
    val allCars by carViewModel.cars
    // If Admin, use allCars. If User, use userCars.
    val cars = if (isAdmin) allCars else userCars

    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var carToDelete by remember { mutableStateOf<CarModel?>(null) }

    LaunchedEffect(Unit) {
        if (isAdmin) {
            carViewModel.fetchCars()
        } else {
            carViewModel.fetchUserCars()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isAdmin) "Manage Cars (Admin)" else "My Ads") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        if (cars.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No cars found in database")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(cars) { car ->
                    AdminCarRow(
                        car = car,
                        onDeleteClick = {
                            carToDelete = car
                            showDialog = true
                        },
                        onEditClick = {
                            onEditCar(car.id)
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }

    if (showDialog && carToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Delete Car?") },
            text = { Text("Are you sure you want to remove ${carToDelete?.title} permanently?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        carToDelete?.let { car ->
                            carViewModel.deleteCar(
                                carId = car.id,
                                onSuccess = {
                                    Toast.makeText(context, "Car Deleted", Toast.LENGTH_SHORT).show()
                                    showDialog = false
                                },
                                onError = {
                                    Toast.makeText(context, "Error: $it", Toast.LENGTH_SHORT).show()
                                    showDialog = false
                                }
                            )
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun AdminCarRow(
    car: CarModel,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = coil.request.ImageRequest.Builder(LocalContext.current)
                .data(car.picUrl)
                .crossfade(true)
                .error(R.drawable.car)
                .fallback(R.drawable.car)
                .build(),
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
            placeholder = androidx.compose.ui.res.painterResource(com.zivkovic.project250.R.drawable.car)
        )
        
        Spacer(Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(text = car.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "$${car.priceInt}", color = Color.Gray, fontSize = 14.sp)
        }
        
        Row {
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
            }
        }
    }
}
