package com.dwan.data.network

import com.dwan.data.source.remote.AttractionEntity
import com.dwan.data.source.remote.AttractionPageEntity
import com.dwan.data.source.remote.CityEntity
import com.dwan.data.source.remote.CountryEntity
import com.dwan.data.source.remote.PresidentEntity
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ColombiaApi {

    @GET("country/Colombia")
    suspend fun getCountry(): CountryEntity

    @GET("President")
    suspend fun getPresidentList(): List<PresidentEntity>

    @GET("President/{id}")
    suspend fun getPresidentDetail(@Path("id") id: Int): PresidentEntity

    @GET("President/search/{keyword}")
    suspend fun getPresidentBySearch(@Path("keyword") word: String): List<PresidentEntity>

    @GET("TouristicAttraction/{id}")
    suspend fun getAttractionDetail(@Path("id") id: Int): AttractionEntity

    @GET("TouristicAttraction/pagedList")
    suspend fun getAttractionByPage(
        @Query("Page") page: String,
        @Query("PageSize") limit: String
    ): AttractionPageEntity

    @GET("TouristicAttraction/search/{keyword}")
    suspend fun getAttractionBySearch(
        @Path("keyword") word: String
    ): List<AttractionEntity>

    @GET("City/{id}")
    suspend fun getCity(
        @Path("id") id: Int
    ): CityEntity
}