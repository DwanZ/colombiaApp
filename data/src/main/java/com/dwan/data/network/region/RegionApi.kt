package com.dwan.data.network.region

import com.dwan.data.source.remote.RegionEntity
import retrofit2.http.GET

interface RegionApi {
    @GET("region/")
    suspend fun getRegions(): List<RegionEntity>

}