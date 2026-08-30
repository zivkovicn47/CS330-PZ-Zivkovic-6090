package com.zivkovic.project250.ui.feature.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zivkovic.project250.R
import com.zivkovic.project250.domain.CarModel

@Composable
fun DetailScreen(
    car: CarModel,
    onBack: () -> Unit,
    onFav: () -> Unit = {},
    onBuyNow: () -> Unit = {},
    isFavorite: Boolean = false,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    val scroll = rememberScrollState()
    val isOwner = car.userId == com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid

    Box(
        Modifier
            .fillMaxSize()
            .background(colorResource(R.color.white))
    ) {
        DetailHeader(
            picUrl = car.picUrl, 
            onBack = onBack, 
            onFav = onFav, 
            isFavorite = isFavorite,
            isOwner = isOwner,
            onEdit = onEdit,
            onDelete = onDelete
        )
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = 240.dp) // Slightly overlap or just below header
                .background(colorResource(R.color.white)) // Ensure solid background for content
                .verticalScroll(scroll)
        ) {
            DetailInfo(car.title)
            
            DetailPriceBar(car.priceInt, onBuyNow)
            
            Spacer(Modifier.height(24.dp))
            
            DetailSpecs(car)

            Spacer(Modifier.height(24.dp))

            DetailContact(car.phone)

            Spacer(Modifier.height(24.dp))

            androidx.compose.material3.Text(
                text = "Description",
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = colorResource(R.color.black)
            )
            
            androidx.compose.material3.Text(
                text = car.description,
                color = androidx.compose.ui.graphics.Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 32.dp),
                lineHeight = 24.sp
            )
        }
    }
}

@Preview
@Composable
fun DetailScreenPreview() {
    val car = CarModel(
        title = "Tesla Model S",
        description = "A an electric car produced by Tesla, Inc.",
        seats = 5,
        highestSpeed = 250,
        enginePower = 670,
        picUrl = "https://res.cloudinary.com/dhzuuct8o/image/upload/v1769125101/sedan_b8sfwq.png",
        price = 79990,
        productionYear = 2022
    )
    DetailScreen(
        car = car,
        onBack = {},
        onFav = {},
        onBuyNow = {})
}