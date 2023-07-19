package com.dwan.data.source.remote

import com.dwan.domain.model.AttractionModel
import com.dwan.domain.model.AttractionPageModel


data class AttractionPageEntity(
    val page: Int,
    val pageSize: Int,
    val totalRecords: Int,
    val pageCount: Int,
    val data: List<AttractionEntity>
)

data class AttractionEntity(
    val city: CityEntity?,
    val cityId: Int,
    val description: String,
    val id: Int,
    val images: List<String>,
    val latitude: String,
    val longitude: String,
    val name: String
)

fun AttractionPageEntity.toModel() =
    AttractionPageModel(
        page = page,
        pageCount = pageCount,
        totalRecords = totalRecords,
        pageSize = pageSize,
        data = data.map {
            it.toModel()
        }
    )
fun AttractionEntity.toModel(cityName : String = "") =
    AttractionModel(
        cityId = cityId,
        description = description,
        id = id,
        images = images,
        name = name,
        latitude = latitude,
        longitude = longitude,
        cityName = cityName
    )