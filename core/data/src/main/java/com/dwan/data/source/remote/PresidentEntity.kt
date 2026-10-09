package com.dwan.data.source.remote

import com.dwan.common.image.sanitizeImageUrl
import com.dwan.domain.model.PresidentModel

data class PresidentEntity(
    val city: CityEntity? = null,
    val cityId: Int = 0,
    val description: String = "",
    val endPeriodDate: String? = null,
    val id: Int = 0,
    val image: String? = null,
    val lastName: String = "",
    val name: String = "",
    val politicalParty: String = "",
    val startPeriodDate: String = ""
)

fun PresidentEntity.toModel() =
    PresidentModel(
        city = city?.toModel(),
        cityId = cityId,
        description = description,
        endPeriodDate = endPeriodDate ?: "Vigente",
        id = id,
        image = sanitizeImageUrl(image).orEmpty(),
        lastName = lastName,
        name = name,
        politicalParty = politicalParty,
        startPeriodDate = startPeriodDate
    )