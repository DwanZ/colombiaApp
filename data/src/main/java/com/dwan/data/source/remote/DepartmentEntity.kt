package com.dwan.data.source.remote

data class DepartmentEntity(
    val cities: List<CityEntity>,
    val cityCapital: CityEntity,
    val cityCapitalId: Int,
    val country: CountryEntity?,
    val countryId: Int,
    val description: String,
    val id: Int,
    val maps: Any?,
    val municipalities: Int,
    val name: String,
    val naturalAreas: List<NaturalAreaEntity>?,
    val phonePrefix: String,
    val population: Int,
    val region: RegionEntity?,
    val regionId: Int,
    val surface: Int
)