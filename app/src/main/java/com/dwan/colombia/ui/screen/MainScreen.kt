package com.dwan.colombia.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dwan.colombia.ui.navbar.BottomBar
import com.dwan.colombia.ui.navbar.NavigationGraph
import com.dwan.colombia.ui.theme.ColombiaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
    val topBarState = rememberSaveable { (mutableStateOf(false)) }
    ColombiaTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        // Control TopBar and BottomBar
        when (navBackStackEntry?.destination?.route) {
            "country" -> {
                bottomBarState.value = true
                topBarState.value = false
            }
            "attractions" -> {
                bottomBarState.value = true
                topBarState.value = false
            }
            "presidents" -> {
                bottomBarState.value = true
                topBarState.value = false
            }
            "presidentDetail" -> {
                bottomBarState.value = false
                topBarState.value = true
            }
        }
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            Scaffold(
                topBar = {
                    /*TopAppBar(
                        title = {
                                Text(text = "")
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack()})
                        }
                    )*/
                },
                bottomBar = {
                    if (currentRoute(navController)) {
                        BottomBar(navController = navController)
                    }
                }
            ) { padding ->
                Box(Modifier.padding(padding)) {
                    NavigationGraph(
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): Boolean {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return when(navBackStackEntry?.destination?.route) {
        "country", "presidents", "attractions" -> true
        else -> false
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ColombiaTheme {
        MainScreen()
    }
}