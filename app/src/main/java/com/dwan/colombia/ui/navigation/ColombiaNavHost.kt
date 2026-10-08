package com.dwan.colombia.ui.navigation

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dwan.feature.attractions.AttractionScreen
import com.dwan.feature.attractions.detail.AttractionDetailScreen
import com.dwan.feature.country.CountryScreen
import com.dwan.feature.map.MapScreen
import com.dwan.feature.presidents.PresidentScreen
import com.dwan.feature.presidents.detail.PresidentDetailScreen

@Composable
fun ColombiaNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Country.route
    ) {
        composable(BottomNavItem.Country.route) {
            CountryScreen()
        }
        composable(BottomNavItem.Map.route) {
            MapScreen(
                onAttractionClick = { id ->
                    navController.navigate("attractionDetail/$id")
                }
            )
        }
        composable(BottomNavItem.Attractions.route) {
            AttractionScreen(
                goToAttractionDetail = { id ->
                    navController.navigate("attractionDetail/$id")
                }
            )
        }
        composable(BottomNavItem.Presidents.route) {
            PresidentScreen(
                goToPresidentDetail = { id ->
                    navController.navigate("presidentDetail/$id")
                }
            )
        }
        composable(
            route = "presidentDetail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType }),
            enterTransition = { slideInVertically { it } },
            popExitTransition = { slideOutVertically { it } }
        ) {
            PresidentDetailScreen(onBack = { navController.popBackStack() })
        }
        composable(
            route = "attractionDetail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType }),
            enterTransition = { slideInVertically { it } },
            popExitTransition = { slideOutVertically { it } }
        ) {
            AttractionDetailScreen(onBack = { navController.popBackStack() })
        }
    }
}
