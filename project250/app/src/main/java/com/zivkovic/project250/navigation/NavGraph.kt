package com.zivkovic.project250.navigation

import android.telecom.Call
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.zivkovic.project250.domain.CarModel
import com.zivkovic.project250.ui.feature.detail.DetailScreen
import com.zivkovic.project250.ui.feature.home.*
import com.zivkovic.project250.ui.feature.profile.ProfileScreen
import com.zivkovic.project250.viewModel.CarViewModel
import com.zivkovic.project250.viewModel.CategoryViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val categoryViewModel: CategoryViewModel = viewModel()
    val carViewModel: CarViewModel = viewModel()
    NavHost(navController = navController, startDestination = Screens.MAIN) {
        composable(Screens.MAIN) {
            MainScreen(
                onProfileClick = { navController.navigate(Screens.PROFILE) },
                onCarClick = { car ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("car", car)
                    navController.navigate(Screens.DETAIL)
                },
                onFavoriteClick = { navController.navigate(Screens.FAVORITES) },
                onAddClick = { navController.navigate(Screens.ADD_CAR) },
                onHomeClick = { /* Already on Home */ },
                carViewModel = carViewModel,
                categoryViewModel = categoryViewModel
            )
        }
        composable(Screens.FAVORITES) {
            com.zivkovic.project250.ui.feature.favorites.FavoritesScreen(
                onProfileClick = { navController.navigate(Screens.PROFILE) },
                onCarClick = { car ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("car", car)
                    navController.navigate(Screens.DETAIL)
                },
                onHomeClick = { navController.navigate(Screens.MAIN) {
                    popUpTo(Screens.MAIN) { inclusive = true }
                } },
                onAddClick = { navController.navigate(Screens.ADD_CAR) },
                carViewModel = carViewModel,
                categoryViewModel = categoryViewModel
            )
        }
        composable(
            route = Screens.ADD_CAR_ROUTE,
            arguments = listOf(navArgument("carId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
             val carId = backStackEntry.arguments?.getString("carId")
             com.zivkovic.project250.ui.feature.addcar.AddCarScreen(
                 carId = carId,
                 onBack = { navController.popBackStack() },
                 carViewModel = carViewModel,
                 categoryViewModel = categoryViewModel
             )
        }
        composable(Screens.PROFILE) {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onManageCarsClick = { navController.navigate("manage_cars_admin?isAdmin=false") },
                onAdminManageCarsClick = { navController.navigate("manage_cars_admin?isAdmin=true") },
                onEditProfileClick = { navController.navigate(Screens.EDIT_PROFILE) },
                onFavoritesClick = { navController.navigate(Screens.FAVORITES) }
            )
        }
        composable(Screens.EDIT_PROFILE) {
            com.zivkovic.project250.ui.feature.profile.EditProfileScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screens.MANAGE_CARS,
            arguments = listOf(navArgument("isAdmin") {
                type = NavType.BoolType
                defaultValue = false
            })
        ) { backStackEntry ->
            val isAdmin = backStackEntry.arguments?.getBoolean("isAdmin") ?: false
            com.zivkovic.project250.ui.feature.managecars.ManageCarsScreen(
                onBack = { navController.popBackStack() },
                onEditCar = { carId ->
                    navController.navigate("add_car_screen?carId=$carId")
                },
                carViewModel = carViewModel,
                isAdmin = isAdmin
            )
        }
        composable(Screens.DETAIL) {
            val passedCar = navController.previousBackStackEntry?.savedStateHandle?.get<CarModel>("car")
            val allCars by carViewModel.cars
            // Prosledjeni objekat je snimak iz trenutka klika. Ako oglas i dalje
            // postoji u listi uzivo, prikazujemo tu verziju - tako se izmena
            // napravljena na web strani vidi bez izlaska sa ekrana.
            val car = allCars.find { it.id == passedCar?.id } ?: passedCar
            if (car != null) {
                val favoriteIds by carViewModel.favoriteIds
                DetailScreen(
                    car = car,
                    onBack = { navController.popBackStack() },
                    onFav = {
                        if (car.id.isNotEmpty()) {
                            carViewModel.toggleFavorite(car.id)
                        }
                    },
                    isFavorite = favoriteIds.contains(car.id),
                    onEdit = {
                        navController.navigate("add_car_screen?carId=${car.id}")
                    },
                    onDelete = {
                        if (car.id.isNotEmpty()) {
                            carViewModel.deleteCar(
                                carId = car.id,
                                onSuccess = { navController.popBackStack() },
                                onError = { }
                            )
                        }
                    }
                )
            }
        }
    }
}


object Screens {
    const val MAIN = "main"
    const val PROFILE = "profile"
    const val DETAIL = "detail"
    const val FAVORITES = "favorites"
    const val ADD_CAR = "add_car_screen"
    const val ADD_CAR_ROUTE = "add_car_screen?carId={carId}"
    const val MANAGE_CARS = "manage_cars_admin?isAdmin={isAdmin}" // Updated route
    const val EDIT_PROFILE = "edit_profile"
}