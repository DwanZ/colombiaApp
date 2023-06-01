package com.dwan.colombia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.dwan.colombia.ui.screen.MainScreen
import com.dwan.colombia.ui.theme.ColombiaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ColombiaTheme {
                MainScreen()
            }
        }
    }
}
