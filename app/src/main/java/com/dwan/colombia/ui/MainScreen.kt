package com.dwan.colombia.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dwan.colombia.ui.navigation.BottomBar
import com.dwan.colombia.ui.navigation.ColombiaNavHost
import com.dwan.colombia.ui.theme.ColombiaTheme

@Composable
fun MainScreen() {
    ColombiaTheme {
        val navController = rememberNavController()
        Surface {
            Scaffold(
                bottomBar = {
                    if (shouldShowBottomBar(navController)) {
                        BottomBar(navController = navController)
                    }
                }
            ) { padding ->
                Box(Modifier.padding(padding)) {
                    ColombiaNavHost(navController = navController)
                }
            }
        }
    }
}

@Composable
private fun shouldShowBottomBar(navController: NavHostController): Boolean {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return when (navBackStackEntry?.destination?.route) {
        "country", "map", "attractions", "presidents" -> true
        else -> false
    }
}
