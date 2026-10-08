package com.dwan.data.source.remote

import com.dwan.domain.model.PresidentModel

data class PresidentEntity(
    val city: CityEntity?,
    val cityId: Int,
    val description: String,
    val endPeriodDate: String?,
    val id: Int,
    val image: String,
    val lastName: String,
    val name: String,
    val politicalParty: String,
    val startPeriodDate: String
)

fun PresidentEntity.toModel() =
    PresidentModel(
        city = city?.toModel(),
        cityId = cityId,
        description = description,
        endPeriodDate = endPeriodDate?:"Vigente",
        id = id,
        image = image,
        lastName = lastName,
        name = name,
        politicalParty = politicalParty,
        startPeriodDate = startPeriodDate
    )