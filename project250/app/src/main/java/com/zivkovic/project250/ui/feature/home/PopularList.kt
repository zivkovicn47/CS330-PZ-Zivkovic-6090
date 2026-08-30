package com.zivkovic.project250.ui.feature.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder

import androidx.compose.foundation.background
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.zivkovic.project250.domain.CarModel
import com.zivkovic.project250.R
import androidx.compose.ui.res.painterResource

@Composable
fun PopularList(
    cars: List<CarModel>,
    onCarClick: (CarModel) -> Unit,
    favoriteIds: Set<String>,
    onFavoriteClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        items(cars) { car ->
            val isFavorite = favoriteIds.contains(car.id)
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(color = colorResource(R.color.gray))
                    .clickable { onCarClick(car) },
                shape = RoundedCornerShape(10.dp),
            ) {
                Box {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        coil.compose.AsyncImage(
                            model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                                .data(car.picUrl)
                                .crossfade(true)
                                .error(R.drawable.car)
                                .fallback(R.drawable.car)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .height(130.dp)
                                .fillMaxWidth(),
                            contentScale = ContentScale.Crop,
                             placeholder = painterResource(R.drawable.car)
                        )
                        Text(
                            text = car.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = "$${car.priceInt}",
                            fontSize = 14.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    // Heart Icon
                    androidx.compose.material3.Surface(
                        shape = androidx.compose.foundation.shape.CircleShape,
                        color = androidx.compose.ui.graphics.Color.White,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(32.dp)
                            .clickable { onFavoriteClick(car.id) }
                    ) {
                        androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                            androidx.compose.material3.Icon(
                                imageVector = if (isFavorite) androidx.compose.material.icons.Icons.Filled.Favorite else androidx.compose.material.icons.Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isFavorite) androidx.compose.ui.graphics.Color.Red else androidx.compose.ui.graphics.Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PopularListPreview() {
    val cars = listOf(
        CarModel(
            title = "Toyota Camry",
            description = "A reliable and comfortable sedan.",
            seats = 5,
            highestSpeed = 180,
            enginePower = 203,
            picUrl = "https://example.com/camry.jpg",
            price = 25000,
            productionYear = 2022
        ),
        CarModel(
            title = "Honda Civic",
            description = "A fuel-efficient and sporty compact car.",
            seats = 5,
            highestSpeed = 190,
            enginePower = 158,
            picUrl = "https://example.com/civic.jpg",
            price = 22000,
            productionYear = 2023
        )
    )
    PopularList(cars = cars, onCarClick = {}, favoriteIds = emptySet(), onFavoriteClick = {})
}