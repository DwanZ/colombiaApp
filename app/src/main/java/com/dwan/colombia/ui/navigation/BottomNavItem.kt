package com.dwan.colombia.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Park
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    data object Country : BottomNavItem("Colombia", Icons.Filled.Flag, "country")
    data object Map : BottomNavItem("Map", Icons.Filled.Map, "map")
    data object Attractions : BottomNavItem("Places", Icons.Filled.Park, "attractions")
    data object Presidents : BottomNavItem("Presidents", Icons.Filled.AccountBalance, "presidents")
}
