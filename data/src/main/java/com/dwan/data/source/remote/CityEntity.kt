package com.dwan.data.source.remote

import com.dwan.domain.model.CityModel

data class CityEntity(
    val departament: DepartmentEntity?,
    val departamentId: Int,
    val description: String,
    val id: Int,
    val name: String,
    val population: Int,
    val postalCode: String?,
    val surface: Int
)


fun CityEntity.toModel() =
    CityModel(
        departamentId = departamentId,
        description = description,
        id = id,
        name = name,
        population = population,
        postalCode = postalCode ?: "",
        surface = surface
    )