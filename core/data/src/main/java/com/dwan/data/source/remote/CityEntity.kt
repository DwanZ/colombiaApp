package com.dwan.data.source.remote

import com.dwan.domain.model.CityModel

data class CityEntity(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val departamentId: Int = 0,
    val population: Int = 0,
    val postalCode: String? = null,
    val surface: Int = 0
)

fun CityEntity.toModel() = CityModel(
    departamentId = departamentId,
    description = description,
    id = id,
    name = name,
    population = population,
    postalCode = postalCode.orEmpty(),
    surface = surface
)
