package com.dwan.data.source

import com.dwan.common.Either
import com.dwan.data.source.remote.AttractionEntity
import com.dwan.data.source.remote.AttractionPageEntity

interface AttractionDataSource {

    suspend fun getAttractionDetail(id: Int): Either<Exception, AttractionEntity>

    suspend fun getAttractionByPage(
        page: String,
        limit: String
    ): Either<Exception, AttractionPageEntity>

    suspend fun getAttractionBySearch(word: String): Either<Exception, List<AttractionEntity>>

    suspend fun getCityName(id: Int): Either<Exception, String>

}