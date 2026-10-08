package com.dwan.domain.repository

import com.dwan.domain.model.AttractionModel
import com.dwan.domain.model.AttractionPageModel

interface AttractionRepository {
    suspend fun getAttractionDetail(id: Int): Result<AttractionModel>
    suspend fun getAttractionByPage(page: String, limit: String): Result<AttractionPageModel>
    suspend fun getAttractionBySearch(word: String): Result<List<AttractionModel>>
}
