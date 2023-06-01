package com.dwan.data.network

import com.dwan.data.source.remote.CountryEntity
import retrofit2.http.GET

interface ColombiaApi {

    @GET("country/Colombia")
    suspend fun getCountry(): CountryEntity

}