package com.dwan.data.network.city

import com.dwan.data.source.remote.CityEntity
import retrofit2.http.GET
import retrofit2.http.Path

interface CityApi {

    @GET("city/{name}")
    suspend fun getCity(@Path("name") name: String): CityEntity
}