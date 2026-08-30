package com.zivkovic.project250.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zivkovic.project250.domain.CarModel
import com.zivkovic.project250.viewModel.CarViewModel
import com.zivkovic.project250.viewModel.CategoryViewModel

@Composable
fun MainScreen(
    onProfileClick: () -> Unit,
    onCarClick: (CarModel) -> Unit,
    onHomeClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onAddClick: () -> Unit,
    carViewModel: CarViewModel,
    categoryViewModel: CategoryViewModel
) {
    val categories by categoryViewModel.categories
    val isLoadingCategory by categoryViewModel.isLoading
    val cars = carViewModel.filteredCars
    val searchText by carViewModel.searchText
    val isLoadingCars by carViewModel.isLoading

    val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
    val username = currentUser?.displayName ?: "User"
    val photoUrl = currentUser?.photoUrl?.toString()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xffefefef))
        ) {
            item {
                HeaderSection(
                    username = username,
                    profileImageUrl = photoUrl,
                    onProfileClick = onProfileClick,
                    onBellClick = {}
                )
            }
            item {
                SearchSection(
                    query = searchText,
                    onQueryChange = { carViewModel.onSearchTextChange(it) }
                )
            }
            item {
                if (isLoadingCategory) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    val selectedCategoryId by carViewModel.selectedCategoryId
                    CategoryList(
                        categories = categories,
                        selectedId = selectedCategoryId,
                        onCategoryClick = { carViewModel.onCategorySelected(it) }
                    )
                }
            }
            item {
                Spacer(Modifier.height(16.dp))
                Column(
                    modifier = Modifier
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
                        Text("Popular Car", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("View All", fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.fillMaxWidth())
                    if (isLoadingCars) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        val favoriteIds by carViewModel.favoriteIds
                        PopularList(
                            cars = cars,
                            onCarClick = onCarClick,
                            favoriteIds = favoriteIds,
                            onFavoriteClick = { carViewModel.toggleFavorite(it) },
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .fillMaxWidth()
                                .height(600.dp),
                            contentPadding = PaddingValues(bottom = 130.dp)
                        )
                    }
                }
            }
        }
        BottomNavBar(
            onProfileClick = onProfileClick,
            onHomeClick = onHomeClick,
            onFavoriteClick = onFavoriteClick,
            onAddClick = onAddClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        )
    }
}
