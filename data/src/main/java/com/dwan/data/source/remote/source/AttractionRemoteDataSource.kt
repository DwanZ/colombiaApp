package com.dwan.data.source.remote.source

import com.dwan.common.Either
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

    override suspend fun getAttractionDetail(id: Int): Either<Exception, AttractionEntity> {
        return try {
            val response = api.getAttractionDetail(id)
            Either.Right(response)
        } catch (e: Exception) {
            Either.Left(e)
        }
    }

    override suspend fun getAttractionByPage(
        page: String,
        limit: String
    ): Either<Exception, AttractionPageEntity> {
        return try {
            val response = api.getAttractionByPage(page, limit)
            Either.Right(response)
        } catch (e: Exception) {
            Either.Left(e)
        }
    }

    override suspend fun getAttractionBySearch(word: String): Either<Exception, List<AttractionEntity>> {
        return try {
            val response = api.getAttractionBySearch(word)
            Either.Right(response)
        } catch (e: Exception) {
            Either.Left(e)
        }
    }

    override suspend fun getCityName(id: Int): Either<Exception, String> {
        return try {
            val response = api.getCity(id)
            Either.Right(response.name)
        } catch (e: Exception) {
            Either.Left(e)
        }
    }
}