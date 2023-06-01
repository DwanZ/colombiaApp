package com.dwan.data.network.naturalArea

import com.dwan.data.source.remote.NaturalAreaEntity
import retrofit2.http.GET

interface NaturalAreaApi {

    @GET("naturalarea")
    suspend fun getNaturalAreas(): List<NaturalAreaEntity>
}