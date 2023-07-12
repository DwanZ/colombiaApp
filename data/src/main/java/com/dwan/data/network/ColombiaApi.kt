package com.dwan.data.network

import com.dwan.data.source.remote.CountryEntity
import com.dwan.data.source.remote.PresidentEntity
import retrofit2.http.GET
import retrofit2.http.Path

interface ColombiaApi {

    @GET("country/Colombia")
    suspend fun getCountry(): CountryEntity

    @GET("President")
    suspend fun getPresidentList(): List<PresidentEntity>

    @GET("President/{id}")
    suspend fun getPresidentDetail(@Path("id") id: Int): PresidentEntity


    @GET("President/search/{keyword}")
    suspend fun getPresidentBySearch(@Path("keyword") word: String): List<PresidentEntity>
}