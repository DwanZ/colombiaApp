package com.dwan.feature.attractions.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dwan.common.BaseViewState
import com.dwan.common.image.ImagePlaceholderKind
import com.dwan.common.image.RemoteImage
import com.dwan.common.ui.ColombiaLoadingView
import com.dwan.common.ui.DescriptionText
import com.dwan.common.ui.GenericErrorView
import com.dwan.common.ui.ScrollableTextSection
import com.dwan.domain.model.AttractionModel

@Composable
fun AttractionDetailScreen(
    onBack: () -> Unit,
    viewModel: AttractionDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    when (val state = uiState) {
        is BaseViewState.Loading -> ColombiaLoadingView()
        is BaseViewState.Failure -> GenericErrorView(
            errorMessage = state.errorMessage,
            action = viewModel::refresh
        )
        is BaseViewState.Success -> AttractionDetailContent(state.data, onBack)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AttractionDetailContent(
    attraction: AttractionModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(attraction.name, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            RemoteImage(
                url = attraction.images.firstOrNull(),
                contentDescription = attraction.name,
                contentScale = ContentScale.Crop,
                placeholderKind = ImagePlaceholderKind.Attraction,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )
            Column(Modifier.padding(16.dp)) {
                Text(
                    attraction.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                if (attraction.cityName.isNotBlank()) {
                    Text(
                        attraction.cityName,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    "Lat ${attraction.latitude} · Lng ${attraction.longitude}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                HorizontalDivider(Modifier.padding(vertical = 12.dp))
                Text(
                    "About",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(8.dp))
                if (attraction.description.length > 600) {
                    ScrollableTextSection(text = attraction.description)
                } else {
                    DescriptionText(text = attraction.description)
                }
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = {
                        val lat = attraction.latitude
                        val lng = attraction.longitude
                        val geoUri = Uri.parse(
                            "geo:$lat,$lng?q=$lat,$lng(${Uri.encode(attraction.name)})"
                        )
                        context.startActivity(Intent(Intent.ACTION_VIEW, geoUri))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Map, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Open in Maps")
                }
            }
        }
    }
}
