package com.dwan.colombia.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Attractions
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material.icons.outlined.Attractions
import androidx.compose.material.icons.outlined.HistoryEdu
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.TravelExplore
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
) {
    data object Country : BottomNavItem(
        title = "Colombia",
        selectedIcon = Icons.Filled.Public,
        unselectedIcon = Icons.Outlined.Public,
        route = "country"
    )

    data object Map : BottomNavItem(
        title = "Map",
        selectedIcon = Icons.Filled.TravelExplore,
        unselectedIcon = Icons.Outlined.TravelExplore,
        route = "map"
    )

    data object Attractions : BottomNavItem(
        title = "Places",
        selectedIcon = Icons.Filled.Attractions,
        unselectedIcon = Icons.Outlined.Attractions,
        route = "attractions"
    )

    data object Presidents : BottomNavItem(
        title = "Presidents",
        selectedIcon = Icons.Filled.HistoryEdu,
        unselectedIcon = Icons.Outlined.HistoryEdu,
        route = "presidents"
    )
}
