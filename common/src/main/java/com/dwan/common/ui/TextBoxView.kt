package com.dwan.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun InformationScrollableBoxView(text: String, modifier: Modifier? = null) {
    Column(
        modifier = modifier ?: Modifier
            .fillMaxWidth()
            .height(200.dp)
            .verticalScroll(rememberScrollState())
            .padding(2.dp)
            .background(MaterialTheme.colorScheme.background),
    ) {
        Text(modifier = Modifier.padding(horizontal = 5.dp, vertical = 10.dp), text = text)
    }
}

@Composable
fun LabeledBoxView(label: String, description: String, background: Color = MaterialTheme.colorScheme.background) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .background(background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 18.dp),
            text = label,
            fontWeight = Bold,
            textAlign = TextAlign.End
        )
        Text(description, Modifier.padding(end = 5.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun BoxPreview() {
    Column() {
        InformationScrollableBoxView(
            "Colombia, officially the Republic of Colombia, is a country in South America " +
                    "with insular regions  is a country in South America with ountry in South America " +
                    "insular regions in North America—near Nicaragua's Caribbean coast—as well as in " +
                    "the Pacific Ocean. The Colombian mainland is bordered by the Caribbean Sea to the " +
                    "north,Venezuela to the east and northeast, Brazil to the southeast, Ecuador and " +
                    "Peru to the south and ish is the official state language,although English and 64 " +
                    "other languages are recognized regional languages.", Modifier
        )
        LabeledBoxView("label", "Description")
    }
}