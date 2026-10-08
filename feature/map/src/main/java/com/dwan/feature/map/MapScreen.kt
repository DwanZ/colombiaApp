package com.dwan.feature.map

import android.graphics.Color as AndroidColor
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
import org.maplibre.android.style.layers.FillLayer
import org.maplibre.android.style.layers.LineLayer
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Feature

private const val SOURCE_ID = "departments-source"
private const val FILL_LAYER_ID = "departments-fill"
private const val LINE_LAYER_ID = "departments-line"
private const val HIGHLIGHT_SOURCE_ID = "departments-highlight"
private const val HIGHLIGHT_LAYER_ID = "departments-highlight-fill"

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
                    departmentName = uiState.selectedDepartment?.name ?: "Department",
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
                    map.setStyle(
                        Style.Builder().fromUri("https://demotiles.maplibre.org/style.json")
                    ) { style ->
                        val geoJson = ctx.assets.open("colombia_departments.geojson")
                            .bufferedReader()
                            .use { it.readText() }
                        style.addSource(GeoJsonSource(SOURCE_ID, geoJson))
                        style.addLayer(
                            FillLayer(FILL_LAYER_ID, SOURCE_ID).withProperties(
                                PropertyFactory.fillColor(AndroidColor.parseColor("#1E88E5")),
                                PropertyFactory.fillOpacity(0.35f)
                            )
                        )
                        style.addLayer(
                            LineLayer(LINE_LAYER_ID, SOURCE_ID).withProperties(
                                PropertyFactory.lineColor(AndroidColor.parseColor("#0D47A1")),
                                PropertyFactory.lineWidth(1.2f)
                            )
                        )
                        style.addSource(GeoJsonSource(HIGHLIGHT_SOURCE_ID))
                        style.addLayer(
                            FillLayer(HIGHLIGHT_LAYER_ID, HIGHLIGHT_SOURCE_ID).withProperties(
                                PropertyFactory.fillColor(AndroidColor.parseColor("#FABC1E")),
                                PropertyFactory.fillOpacity(0.55f)
                            )
                        )
                    }
                    map.addOnMapClickListener { point ->
                        val screen = map.projection.toScreenLocation(point)
                        val features = map.queryRenderedFeatures(screen, FILL_LAYER_ID)
                        val feature = features.firstOrNull()
                        val name = feature?.getStringProperty("name").orEmpty()
                        if (name.isNotBlank()) {
                            highlightFeature(map.style, feature)
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
                map.style?.let { style ->
                    // keep highlight in sync when selection changes externally
                    if (selectedName.isNullOrBlank()) {
                        (style.getSource(HIGHLIGHT_SOURCE_ID) as? GeoJsonSource)
                            ?.setGeoJson(
                                org.maplibre.geojson.FeatureCollection.fromFeatures(emptyList())
                            )
                    }
                }
            }
        },
        onRelease = { mapView ->
            mapView.onDestroy()
        }
    )
}

private fun highlightFeature(style: Style?, feature: Feature?) {
    if (style == null || feature == null) return
    val source = style.getSource(HIGHLIGHT_SOURCE_ID) as? GeoJsonSource ?: return
    source.setGeoJson(org.maplibre.geojson.FeatureCollection.fromFeature(feature))
}

@Composable
private fun DepartmentAttractionsSheet(
    departmentName: String,
    attractionsState: BaseViewState<List<AttractionModel>>?,
    onAttractionClick: (Int) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .heightIn(min = 180.dp, max = 480.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = departmentName,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Touristic places",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
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
                    Text("No touristic attractions found for this department.")
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
                                        style = MaterialTheme.typography.bodySmall
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
