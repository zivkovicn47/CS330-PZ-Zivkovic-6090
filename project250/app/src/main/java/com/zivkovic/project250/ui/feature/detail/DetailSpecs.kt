package com.zivkovic.project250.ui.feature.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zivkovic.project250.R
import com.zivkovic.project250.ui.components.SpecCard



import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import com.zivkovic.project250.domain.CarModel

@Composable
fun DetailSpecs(car: CarModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Row 1: Year & Mileage
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SpecCard(Icons.Default.DateRange, "Year", "${car.productionYearInt}", Modifier.weight(1f))
            SpecCard(R.drawable.speed, "Mileage", "${car.mileageInt} km", Modifier.weight(1f)) // Reuse speed icon or find better
        }
        // Row 2: Fuel & Transmission
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Placeholder icons if specific ones absent
            SpecCard(Icons.Default.Info, "Fuel", car.fuelType.ifEmpty { "N/A" }, Modifier.weight(1f))
            SpecCard(Icons.Default.Settings, "Gearbox", car.transmission.ifEmpty { "N/A" }, Modifier.weight(1f))
        }
        // Row 3: Engine Power & Volume
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SpecCard(R.drawable.engine, "Power", "${car.enginePowerInt} hp", Modifier.weight(1f))
            SpecCard(Icons.Default.Info, "Volume", "${car.engineVolumeInt} cc", Modifier.weight(1f))
        }
         // Row 4: Speed & Seats
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SpecCard(R.drawable.speed, "Max Speed", "${car.highestSpeedInt} km/h", Modifier.weight(1f))
            SpecCard(R.drawable.sit, "Seats", "${car.seatsInt} seats", Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
fun DetailSpecsPreview() {
    val car = CarModel(
        title = "Test Car",
        productionYear = 2023,
        mileage = 15000,
        fuelType = "Petrol",
        transmission = "Automatic",
        enginePower = 300,
        engineVolume = 2000,
        highestSpeed = 250,
        seats = 5
    )
    DetailSpecs(car)
}
