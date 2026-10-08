package com.dwan.data.source.remote

import com.dwan.domain.model.DepartmentModel
import com.google.gson.annotations.SerializedName

data class DepartmentEntity(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val cityCapitalId: Int = 0,
    val municipalities: Int = 0,
    val phonePrefix: String = "",
    val population: Int = 0,
    val regionId: Int = 0,
    val surface: Int = 0,
    @SerializedName("cityCapital")
    val cityCapital: CityEntity? = null,
    val cities: List<CityEntity>? = null,
    val naturalAreas: List<NaturalAreaEntity>? = null,
    val region: RegionEntity? = null
)

fun DepartmentEntity.toModel() = DepartmentModel(
    id = id,
    name = name,
    description = description,
    cityCapitalId = cityCapitalId,
    municipalities = municipalities,
    phonePrefix = phonePrefix,
    population = population,
    regionId = regionId,
    surface = surface
)
