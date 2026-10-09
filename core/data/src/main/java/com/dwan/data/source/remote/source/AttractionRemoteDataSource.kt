package com.dwan.data.source.remote.source

import com.dwan.data.network.ColombiaApi
import com.dwan.data.source.AttractionDataSource
import com.dwan.data.source.remote.AttractionEntity
import com.dwan.data.source.remote.AttractionPageEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AttractionRemoteDataSource @Inject constructor(
    private val api: ColombiaApi
) : AttractionDataSource {

    override suspend fun getAttractionDetail(id: Int): Result<AttractionEntity> = runCatching {
        api.getAttractionDetail(id)
    }

    override suspend fun getAttractionList(): Result<List<AttractionEntity>> = runCatching {
        api.getAttractionList()
    }

    override suspend fun getAttractionByPage(
        page: String,
        limit: String
    ): Result<AttractionPageEntity> = runCatching {
        api.getAttractionByPage(page, limit)
    }

    override suspend fun getAttractionBySearch(word: String): Result<List<AttractionEntity>> =
        runCatching { api.getAttractionBySearch(word) }

    override suspend fun getCityName(cityId: Int): Result<String> = runCatching {
        api.getCity(cityId).name
    }
}
