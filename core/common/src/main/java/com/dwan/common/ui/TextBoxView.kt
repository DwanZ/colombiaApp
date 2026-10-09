package com.dwan.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

@Deprecated(
    message = "Use ScrollableTextSection or DescriptionText",
    replaceWith = ReplaceWith("ScrollableTextSection(text)")
)
@Composable
fun InformationScrollableBoxView(text: String, modifier: Modifier? = null) {
    ScrollableTextSection(
        text = text,
        modifier = modifier ?: Modifier.fillMaxWidth()
    )
}

@Composable
fun LabeledBoxView(
    label: String,
    description: String,
    background: Color = MaterialTheme.colorScheme.background
) {
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
    Column {
        ScrollableTextSection(
            "Colombia, officially the Republic of Colombia, is a country in South America " +
                "with insular regions near Nicaragua's Caribbean coast as well as in the Pacific Ocean."
        )
        LabeledBoxView("label", "Description")
    }
}
