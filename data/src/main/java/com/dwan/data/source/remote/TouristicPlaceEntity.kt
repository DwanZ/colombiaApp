package com.dwan.data.source.remote

data class TouristicPlaceEntity(
    val city: CityEntity,
    val cityId: Int,
    val description: String,
    val id: Int,
    val images: List<String>,
    val latitude: String,
    val longitude: String,
    val name: String
)