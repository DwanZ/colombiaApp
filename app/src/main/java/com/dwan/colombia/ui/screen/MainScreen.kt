package com.dwan.colombia.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.dwan.colombia.ui.navbar.BottomBar
import com.dwan.colombia.ui.navbar.NavigationGraph
import com.dwan.colombia.ui.theme.ColombiaTheme

@Composable
fun MainScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = { BottomBar(navController = navController) }
        ) {
            Modifier.padding(it)
            NavigationGraph(
                navController = navController
            )
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ColombiaTheme {
        MainScreen()
    }
}