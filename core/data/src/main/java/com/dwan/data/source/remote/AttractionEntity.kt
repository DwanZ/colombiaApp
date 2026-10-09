package com.dwan.data.source.remote

import com.dwan.domain.model.AttractionModel
import com.dwan.domain.model.AttractionPageModel

data class AttractionPageEntity(
    val page: Int = 0,
    val pageSize: Int = 0,
    val totalRecords: Int = 0,
    val pageCount: Int = 0,
    val data: List<AttractionEntity> = emptyList()
)

data class AttractionEntity(
    val city: CityEntity? = null,
    val cityId: Int = 0,
    val description: String = "",
    val id: Int = 0,
    val images: List<String>? = null,
    val latitude: String = "",
    val longitude: String = "",
    val name: String = ""
)

fun AttractionPageEntity.toModel() = AttractionPageModel(
    page = page,
    pageCount = pageCount,
    totalRecords = totalRecords,
    pageSize = pageSize,
    data = data.map { it.toModel() }
)

fun AttractionEntity.toModel(cityName: String = "") = AttractionModel(
    cityId = cityId,
    description = description,
    id = id,
    images = images.orEmpty(),
    name = name,
    latitude = latitude,
    longitude = longitude,
    cityName = cityName.ifBlank { city?.name.orEmpty() },
    departmentId = city?.departamentId ?: 0
)
