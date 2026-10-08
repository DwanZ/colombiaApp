package com.dwan.domain.model

data class AttractionModel (
    val cityId: Int,
    val description: String,
    val id: Int,
    val images: List<String>,
    val latitude: String,
    val longitude: String,
    val name: String,
    val cityName: String = ""
)

data class AttractionPageModel (
    val page: Int,
    val pageSize: Int,
    val totalRecords: Int,
    val pageCount: Int,
    val data: List<AttractionModel>
)