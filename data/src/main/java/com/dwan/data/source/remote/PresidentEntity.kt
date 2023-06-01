package com.dwan.data.source.remote

data class PresidentEntity(
    val city: CityEntity,
    val cityId: Int,
    val description: String,
    val endPeriodDate: String,
    val id: Int,
    val image: String,
    val lastName: String,
    val name: String,
    val politicalParty: String,
    val startPeriodDate: String
)