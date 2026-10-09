package com.dwan.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Long-form text with an always-visible scrollbar track when content overflows.
 */
@Composable
fun ScrollableTextSection(
    text: String,
    modifier: Modifier = Modifier,
    maxHeight: Dp = 220.dp
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = maxHeight)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f))
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(end = 8.dp)
        )

        BoxWithConstraints(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.75f))
        ) {
            val trackHeightPx = with(LocalDensity.current) { maxHeight.toPx() }
            val canScroll = scrollState.maxValue > 0
            val thumbHeightFraction = if (canScroll) {
                (trackHeightPx / (trackHeightPx + scrollState.maxValue)).coerceIn(0.2f, 1f)
            } else {
                1f
            }
            val thumbHeight = maxHeight * thumbHeightFraction
            val travel = (maxHeight - thumbHeight).coerceAtLeast(0.dp)
            val thumbOffset = if (canScroll) {
                travel * (scrollState.value.toFloat() / scrollState.maxValue.toFloat())
            } else {
                0.dp
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(thumbHeight)
                    .offset(y = thumbOffset)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.9f))
            )
        }
    }
}

/** Plain description for parent-scrolling screens (no nested scroller). */
@Composable
fun DescriptionText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}
