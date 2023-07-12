package com.dwan.colombia.ui.navbar

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dwan.colombia.R
import com.dwan.colombia.ui.screen.attraction.AttractionScreen
import com.dwan.colombia.ui.screen.country.CountryScreen
import com.dwan.colombia.ui.screen.president.PresidentScreen
import com.dwan.colombia.ui.screen.president.detail.PresidentDetailScreen

sealed class BottomNavItem(var title: String, var icon: Int, var screen_route: String) {

    object Country : BottomNavItem("Colombia", R.drawable.ic_colombia_2, "country")
    object Attractions : BottomNavItem("Attractions", R.drawable.ic_nature, "attractions")
    object Presidents :
        BottomNavItem("Presidents", R.drawable.ic_president, "presidents")
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Country.screen_route) {
        composable(BottomNavItem.Country.screen_route) {
            CountryScreen()
        }
        composable(BottomNavItem.Attractions.screen_route) {
            AttractionScreen()
        }
        composable(BottomNavItem.Presidents.screen_route) {
            PresidentScreen() { id ->
                navController.navigate("presidentDetail/$id")
            }
        }
        presidentDetailGraph(navController)
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.presidentDetailGraph(navController: NavHostController) {
    composable(
        "presidentDetail/{id}",
        arguments = listOf(navArgument("id") { type = NavType.IntType }),
        enterTransition = {
            // Let's make for a really long fade in
            slideInVertically(
                initialOffsetY = { 1800 }
            )
        }, popExitTransition = {
            slideOutVertically(
                targetOffsetY = { 1800 }
            )
        }
    ) {
        PresidentDetailScreen(navController)
    }
}
