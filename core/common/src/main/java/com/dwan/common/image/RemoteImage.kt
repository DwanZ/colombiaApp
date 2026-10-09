package com.dwan.common.image

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.dwan.common.R

fun sanitizeImageUrl(url: String?): String? {
    val value = url?.trim().orEmpty()
    if (value.isEmpty() || value.equals("null", ignoreCase = true)) return null
    return value
}

enum class ImagePlaceholderKind {
    President,
    Attraction,
    Generic
}

@Composable
fun RemoteImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    placeholderKind: ImagePlaceholderKind = ImagePlaceholderKind.Generic
) {
    val context = LocalContext.current
    val safeUrl = sanitizeImageUrl(url)
    val fallbackRes = when (placeholderKind) {
        ImagePlaceholderKind.President -> R.drawable.ic_placeholder_president
        ImagePlaceholderKind.Attraction -> R.drawable.ic_placeholder_attraction
        ImagePlaceholderKind.Generic -> R.drawable.ic_placeholder_attraction
    }

    if (safeUrl == null) {
        Image(
            painter = painterResource(fallbackRes),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
        return
    }

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(context)
            .data(safeUrl)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
    ) {
        when (painter.state) {
            is AsyncImagePainter.State.Loading -> {
                ImageSkeleton(showSpinner = true)
            }
            is AsyncImagePainter.State.Error -> {
                Image(
                    painter = painterResource(fallbackRes),
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> SubcomposeAsyncImageContent()
        }
    }
}

@Composable
fun ImageSkeleton(
    modifier: Modifier = Modifier,
    showSpinner: Boolean = true
) {
    val transition = rememberInfiniteTransition(label = "skeleton")
    val alpha by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "skeletonAlpha"
    )
    val base = MaterialTheme.colorScheme.surfaceVariant
    val highlight = MaterialTheme.colorScheme.primaryContainer

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        base.copy(alpha = alpha),
                        highlight.copy(alpha = alpha),
                        base.copy(alpha = alpha)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        if (showSpinner) {
            CircularProgressIndicator(
                modifier = Modifier.size(28.dp),
                strokeWidth = 2.5.dp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
