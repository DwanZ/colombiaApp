package com.dwan.data.source.remote

data class CategoryEntity(
    val description: String,
    val id: Int,
    val name: String,
    val naturalAreas: List<NaturalAreaEntity>
)