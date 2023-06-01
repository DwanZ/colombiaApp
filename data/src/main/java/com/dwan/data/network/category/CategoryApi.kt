package com.dwan.data.network.category

import com.dwan.data.source.remote.CategoryEntity
import retrofit2.http.GET

interface CategoryApi {

    @GET("categoryNaturalArea")
    suspend fun getCategories(): List<CategoryEntity>
}