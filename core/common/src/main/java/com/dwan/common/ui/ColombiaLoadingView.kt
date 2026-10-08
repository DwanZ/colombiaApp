package com.dwan.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ColombiaLoadingView() {
    Column(
        Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                color = Color.Red,
                modifier = Modifier.then(Modifier.size(60.dp))
            )
            CircularProgressIndicator(
                color = Color.Blue,
                modifier = Modifier.then(Modifier.size(70.dp))
            )
            CircularProgressIndicator(
                color = Color.Yellow,
                modifier = Modifier.then(Modifier.size(90.dp)),
                strokeWidth = 10.dp
            )
        }
    }
}


@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun ColombiaLoadingPreview() {
    ColombiaLoadingView()
}

