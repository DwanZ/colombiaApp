package com.dwan.data.repository

import com.dwan.common.Either
import com.dwan.common.getOrElse
import com.dwan.common.map
import com.dwan.common.onFailure
import com.dwan.data.source.AttractionDataSource
import com.dwan.data.source.remote.toModel
import com.dwan.domain.model.AttractionModel
import com.dwan.domain.model.AttractionPageModel
import com.dwan.domain.repository.AttractionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AttractionRepositoryImpl @Inject constructor(
    private val dataSource: AttractionDataSource
) : AttractionRepository {

    override suspend fun getAttractionDetail(id: Int): Either<Exception, AttractionModel> {
        val attraction = withContext(Dispatchers.Default) {
            dataSource.getAttractionDetail(id)
        }
        val cityName = withContext(Dispatchers.Default) {
            dataSource.getCityName(id)
        }
        return when (attraction) {
            is Either.Left -> attraction.onFailure {}
            is Either.Right -> attraction.map {
                it.toModel(cityName.getOrElse(""))
            }
        }
    }
    override suspend fun getAttractionByPage(
        page: String,
        limit: String
    ): Either<Exception, AttractionPageModel> =
        when (val response = dataSource.getAttractionByPage(page, limit)) {
            is Either.Left -> response.onFailure {}
            is Either.Right -> response.map {
                it.toModel()
            }
        }

    override suspend fun getAttractionBySearch(word: String): Either<Exception, List<AttractionModel>> =
        when (val response = dataSource.getAttractionBySearch(word)) {
            is Either.Left -> response.onFailure {}
            is Either.Right -> response.map { list ->
                list.map { it.toModel() }
            }
        }

}


