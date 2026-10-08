package com.dwan.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GenericErrorView(errorMessage: String, action: () -> Unit, buttonText: String = "Retry") {
    Column(
        Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ErrorMessageView(errorMessage = errorMessage)
        RetrieveButton(action = action, text = buttonText)
    }
}

@Preview(showBackground = true)
@Composable
fun GenericErrorPreview() {
    GenericErrorView(errorMessage = "Error preview text content", action = {})
}