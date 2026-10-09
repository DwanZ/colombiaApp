package com.dwan.feature.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dwan.common.BaseViewState
import com.dwan.common.ui.ColombiaLoadingView
import com.dwan.common.ui.GenericErrorView
import com.dwan.domain.model.AttractionModel
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style

/** Muted light basemap so Colombia departments read as the highlight. */
private const val BASEMAP_STYLE_URI = "https://tiles.openfreemap.org/styles/positron"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
    onAttractionClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val appContext = LocalContext.current.applicationContext
    remember(appContext) {
        MapLibre.getInstance(appContext)
        true
    }

    Box(Modifier.fillMaxSize()) {
        when (val departments = uiState.departments) {
            is BaseViewState.Loading -> ColombiaLoadingView()
            is BaseViewState.Failure -> GenericErrorView(
                errorMessage = departments.errorMessage,
                action = viewModel::refresh
            )
            is BaseViewState.Success -> {
                ColombiaMapView(
                    selectedName = uiState.selectedDepartment?.name,
                    onDepartmentName = viewModel::onDepartmentTapped
                )
            }
        }

        if (uiState.sheetVisible) {
            ModalBottomSheet(
                onDismissRequest = viewModel::dismissSheet,
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ) {
                DepartmentAttractionsSheet(
                    department = uiState.selectedDepartment,
                    attractionsState = uiState.attractions,
                    onAttractionClick = onAttractionClick
                )
            }
        }
    }
}

@Composable
private fun ColombiaMapView(
    selectedName: String?,
    onDepartmentName: (String) -> Unit
) {
    val context = LocalContext.current
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            MapView(ctx).apply {
                onCreate(null)
                getMapAsync { map ->
                    map.uiSettings.isAttributionEnabled = true
                    map.cameraPosition = CameraPosition.Builder()
                        .target(LatLng(4.5709, -74.2973))
                        .zoom(4.6)
                        .build()
                    map.setStyle(Style.Builder().fromUri(BASEMAP_STYLE_URI)) { style ->
                        val geoJson = ctx.assets.open("colombia_departments.geojson")
                            .bufferedReader()
                            .use { it.readText() }
                        style.addColombiaFocusLayers(geoJson)
                    }
                    map.addOnMapClickListener { point ->
                        val screen = map.projection.toScreenLocation(point)
                        val features = map.queryRenderedFeatures(
                            screen,
                            DEPARTMENTS_FILL_LAYER_ID
                        )
                        val feature = features.firstOrNull()
                        val name = feature?.getStringProperty("name").orEmpty()
                        if (name.isNotBlank()) {
                            highlightDepartment(map.style, feature)
                            onDepartmentName(name)
                            true
                        } else {
                            false
                        }
                    }
                }
            }
        },
        update = { mapView ->
            mapView.getMapAsync { map ->
                if (selectedName.isNullOrBlank()) {
                    clearDepartmentHighlight(map.style)
                }
            }
        },
        onRelease = { mapView ->
            mapView.onDestroy()
        }
    )
}

@Composable
private fun DepartmentAttractionsSheet(
    department: com.dwan.domain.model.DepartmentModel?,
    attractionsState: BaseViewState<List<AttractionModel>>?,
    onAttractionClick: (Int) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .heightIn(min = 180.dp, max = 520.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = department?.name ?: "Department",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        if (department != null) {
            Text(
                text = "Population %,d · %,d km²".format(department.population, department.surface),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )
            if (department.description.isNotBlank()) {
                Text(
                    text = department.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
        Text(
            text = "Touristic places",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
        )
        HorizontalDivider()
        Spacer(Modifier.height(8.dp))
        when (attractionsState) {
            null, is BaseViewState.Loading -> {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ColombiaLoadingView()
                }
            }
            is BaseViewState.Failure -> Text(
                attractionsState.errorMessage,
                color = MaterialTheme.colorScheme.error
            )
            is BaseViewState.Success -> {
                if (attractionsState.data.isEmpty()) {
                    Text(
                        "No touristic attractions listed for this department yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    LazyColumn {
                        items(attractionsState.data, key = { it.id }) { attraction ->
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable { onAttractionClick(attraction.id) }
                                    .padding(vertical = 10.dp)
                            ) {
                                Text(attraction.name, fontWeight = FontWeight.SemiBold)
                                if (attraction.cityName.isNotBlank()) {
                                    Text(
                                        attraction.cityName,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}
