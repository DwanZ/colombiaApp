package com.dwan.feature.map

import android.graphics.Color as AndroidColor
import org.maplibre.android.maps.Style
import org.maplibre.android.style.expressions.Expression
import org.maplibre.android.style.layers.FillLayer
import org.maplibre.android.style.layers.LineLayer
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection

internal const val WORLD_MASK_SOURCE_ID = "world-mask-source"
internal const val WORLD_MASK_LAYER_ID = "world-mask-fill"
internal const val DEPARTMENTS_SOURCE_ID = "departments-source"
internal const val DEPARTMENTS_FILL_LAYER_ID = "departments-fill"
internal const val DEPARTMENTS_LINE_LAYER_ID = "departments-line"
internal const val HIGHLIGHT_SOURCE_ID = "departments-highlight"
internal const val HIGHLIGHT_LAYER_ID = "departments-highlight-fill"

/** Soft grey wash over the basemap so Colombia becomes the visual focus. */
private const val MASK_COLOR = "#78909C"

/**
 * Distinct department fills (flag-adjacent blues/golds/reds + readable accents).
 * Indexed by DANE `code` from colombia_departments.geojson.
 */
private val DEPARTMENT_FILL_BY_CODE = linkedMapOf(
    "05" to "#1B4F9C", // Antioquia
    "08" to "#00897B", // Atlántico
    "11" to "#F9A825", // Bogotá
    "13" to "#C62828", // Bolívar
    "15" to "#6A1B9A", // Boyacá
    "17" to "#2E7D32", // Caldas
    "18" to "#EF6C00", // Caquetá
    "19" to "#0277BD", // Cauca
    "20" to "#AD1457", // Cesar
    "23" to "#558B2F", // Córdoba
    "25" to "#4527A0", // Cundinamarca
    "27" to "#00695C", // Chocó
    "41" to "#D84315", // Huila
    "44" to "#1565C0", // La Guajira
    "47" to "#7B1FA2", // Magdalena
    "50" to "#43A047", // Meta
    "52" to "#00838F", // Nariño
    "54" to "#E65100", // Norte de Santander
    "63" to "#5D4037", // Quindío
    "66" to "#283593", // Risaralda
    "68" to "#C62828", // Santander
    "70" to "#0D7377", // Sucre
    "73" to "#6A1B9A", // Tolima
    "76" to "#1E88E5", // Valle del Cauca
    "81" to "#F9A825", // Arauca
    "85" to "#2E7D32", // Casanare
    "86" to "#EF6C00", // Putumayo
    "88" to "#0277BD", // San Andrés
    "91" to "#00897B", // Amazonas
    "94" to "#AD1457", // Guainía
    "95" to "#558B2F", // Guaviare
    "97" to "#4527A0", // Vaupés
    "99" to "#D84315"  // Vichada
)

private const val WORLD_MASK_GEOJSON = """
{
  "type": "Feature",
  "properties": {},
  "geometry": {
    "type": "Polygon",
    "coordinates": [[
      [-180.0, -85.0],
      [180.0, -85.0],
      [180.0, 85.0],
      [-180.0, 85.0],
      [-180.0, -85.0]
    ]]
  }
}
"""

internal fun Style.addColombiaFocusLayers(departmentsGeoJson: String) {
    addSource(GeoJsonSource(WORLD_MASK_SOURCE_ID, WORLD_MASK_GEOJSON.trim()))
    addLayer(
        FillLayer(WORLD_MASK_LAYER_ID, WORLD_MASK_SOURCE_ID).withProperties(
            PropertyFactory.fillColor(AndroidColor.parseColor(MASK_COLOR)),
            PropertyFactory.fillOpacity(0.64f),
            PropertyFactory.fillAntialias(true)
        )
    )

    addSource(GeoJsonSource(DEPARTMENTS_SOURCE_ID, departmentsGeoJson))
    addLayer(
        FillLayer(DEPARTMENTS_FILL_LAYER_ID, DEPARTMENTS_SOURCE_ID).withProperties(
            PropertyFactory.fillColor(departmentFillColorExpression()),
            PropertyFactory.fillOpacity(0.78f),
            PropertyFactory.fillAntialias(true)
        )
    )
    addLayer(
        LineLayer(DEPARTMENTS_LINE_LAYER_ID, DEPARTMENTS_SOURCE_ID).withProperties(
            PropertyFactory.lineColor(AndroidColor.parseColor("#0D47A1")),
            PropertyFactory.lineWidth(1.5f),
            PropertyFactory.lineOpacity(0.95f)
        )
    )

    addSource(GeoJsonSource(HIGHLIGHT_SOURCE_ID))
    addLayer(
        FillLayer(HIGHLIGHT_LAYER_ID, HIGHLIGHT_SOURCE_ID).withProperties(
            PropertyFactory.fillColor(AndroidColor.parseColor("#FCD116")),
            PropertyFactory.fillOpacity(0.55f)
        )
    )
}

private fun departmentFillColorExpression(): Expression {
    val stops = DEPARTMENT_FILL_BY_CODE.map { (code, hex) ->
        Expression.stop(code, Expression.color(AndroidColor.parseColor(hex)))
    }.toTypedArray()
    return Expression.match(
        Expression.get("code"),
        Expression.color(AndroidColor.parseColor("#1976D2")),
        *stops
    )
}

internal fun clearDepartmentHighlight(style: Style?) {
    val source = style?.getSource(HIGHLIGHT_SOURCE_ID) as? GeoJsonSource ?: return
    source.setGeoJson(FeatureCollection.fromFeatures(emptyList()))
}

internal fun highlightDepartment(style: Style?, feature: Feature?) {
    if (style == null || feature == null) return
    val source = style.getSource(HIGHLIGHT_SOURCE_ID) as? GeoJsonSource ?: return
    source.setGeoJson(FeatureCollection.fromFeature(feature))
}
