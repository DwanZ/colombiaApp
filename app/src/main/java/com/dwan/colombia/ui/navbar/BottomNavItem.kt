package com.dwan.colombia.ui.navbar

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dwan.colombia.R
import com.dwan.colombia.ui.screen.country.CountryScreen

sealed class BottomNavItem(var title: String, var icon: Int, var screen_route: String) {

    object Country : BottomNavItem("Colombia", R.drawable.ic_colombia_2, "country")
    object Attractions : BottomNavItem("Attractions", R.drawable.ic_nature, "attractions")
    object Presidents :
        BottomNavItem("Presidents", R.drawable.ic_president, "presidents")
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Country.screen_route) {
        composable(BottomNavItem.Country.screen_route) {
            CountryScreen()
        }
        composable(BottomNavItem.Attractions.screen_route) {
            //DashboardScreenView()
        }
        composable(BottomNavItem.Presidents.screen_route) {
            //NotificationScreenView(NotificationsViewModel())
        }
    }
}
