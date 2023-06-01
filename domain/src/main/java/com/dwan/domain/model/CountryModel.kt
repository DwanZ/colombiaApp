package com.dwan.domain.model

data class CountryModel(
    val aircraftPrefix: String,
    val borders: List<String>,
    val currency: String,
    val currencyCode: String,
    val currencySymbol: String,
    val description: String,
    val flags: List<String>,
    val id: Int,
    val internetDomain: String,
    val isoCode: String,
    val languages: List<String>,
    val name: String,
    val phonePrefix: String,
    val population: Int,
    val radioPrefix: String,
    val region: String,
    val stateCapital: String,
    val subRegion: String,
    val surface: Int,
    val timeZone: String
)