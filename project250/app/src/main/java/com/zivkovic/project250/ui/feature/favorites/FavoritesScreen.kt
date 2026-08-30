package com.zivkovic.project250.ui.feature.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zivkovic.project250.domain.CarModel
import com.zivkovic.project250.ui.feature.home.BottomNavBar
import com.zivkovic.project250.ui.feature.home.HeaderSection
import com.zivkovic.project250.ui.feature.home.PopularList
import com.zivkovic.project250.ui.feature.home.SearchSection
import com.zivkovic.project250.viewModel.CarViewModel

@Composable
fun FavoritesScreen(
    onProfileClick: () -> Unit,
    onCarClick: (CarModel) -> Unit,
    onHomeClick: () -> Unit,
    onAddClick: () -> Unit,
    carViewModel: CarViewModel,
    categoryViewModel: com.zivkovic.project250.viewModel.CategoryViewModel
) {
    val favoriteCars = carViewModel.filteredFavoriteCars
    val favoriteIds by carViewModel.favoriteIds
    val searchText by carViewModel.searchText
    val isLoadingCars by carViewModel.isLoading

    val categories by categoryViewModel.categories
    val isLoadingCategory by categoryViewModel.isLoading

    val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
    val username = currentUser?.displayName ?: "User"
    val photoUrl = currentUser?.photoUrl?.toString()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xffefefef))
        ) {
            HeaderSection(
                username = username,
                profileImageUrl = photoUrl,
                onProfileClick = onProfileClick,
                onBellClick = {}
            )
            SearchSection(
                query = searchText,
                onQueryChange = { carViewModel.onSearchTextChange(it) }
            )
            
            if (isLoadingCategory) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                val selectedCategoryId by carViewModel.selectedCategoryId
                com.zivkovic.project250.ui.feature.home.CategoryList(
                    categories = categories,
                    selectedId = selectedCategoryId,
                    onCategoryClick = { carViewModel.onCategorySelected(it) }
                )
            }
            
            Spacer(Modifier.height(16.dp))
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        Color.White,
                        RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = 24.dp, start = 16.dp, end = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Favorite Cars", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                if (isLoadingCars) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (favoriteCars.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No favorites yet", fontSize = 16.sp, color = Color.Gray)
                    }
                } else {
                    PopularList(
                        cars = favoriteCars,
                        onCarClick = onCarClick,
                        favoriteIds = favoriteIds,
                        onFavoriteClick = { carViewModel.toggleFavorite(it) },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        contentPadding = PaddingValues(top = 16.dp, bottom = 130.dp)
                    )
                }
            }
        }
        BottomNavBar(
            onProfileClick = onProfileClick,
            onHomeClick = onHomeClick,
            onFavoriteClick = { /* Already on favorites */ },
            onAddClick = onAddClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        )
    }
}
