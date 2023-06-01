package com.dwan.data.source.remote

data class CityEntity(
    val departament: DepartmentEntity?,
    val departamentId: Int,
    val description: String,
    val id: Int,
    val name: String,
    val population: Int,
    val postalCode: String,
    val presidents: List<PresidentEntity>?,
    val surface: Int,
    val touristAttractions: TouristicPlaceEntity?
)