package com.dwan.data.source

import com.dwan.data.source.remote.AttractionEntity
import com.dwan.data.source.remote.AttractionPageEntity

interface AttractionDataSource {
    suspend fun getAttractionDetail(id: Int): Result<AttractionEntity>
    suspend fun getAttractionList(): Result<List<AttractionEntity>>
    suspend fun getAttractionByPage(page: String, limit: String): Result<AttractionPageEntity>
    suspend fun getAttractionBySearch(word: String): Result<List<AttractionEntity>>
    suspend fun getCityName(cityId: Int): Result<String>
}
