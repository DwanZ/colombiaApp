package com.dwan.domain.repository

import com.dwan.common.Either
import com.dwan.domain.model.AttractionModel
import com.dwan.domain.model.AttractionPageModel

interface AttractionRepository {

    suspend fun getAttractionDetail(id: Int): Either<Exception, AttractionModel>

    suspend fun getAttractionByPage(page: String, limit: String): Either<Exception, AttractionPageModel>

    suspend fun getAttractionBySearch(word: String): Either<Exception, List<AttractionModel>>

}