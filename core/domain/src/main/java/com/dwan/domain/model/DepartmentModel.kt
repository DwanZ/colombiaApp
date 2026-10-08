package com.dwan.domain.model

data class DepartmentModel(
    val id: Int,
    val name: String,
    val description: String,
    val cityCapitalId: Int,
    val municipalities: Int,
    val phonePrefix: String,
    val population: Int,
    val regionId: Int,
    val surface: Int
)
