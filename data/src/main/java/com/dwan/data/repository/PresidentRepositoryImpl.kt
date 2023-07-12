package com.dwan.data.repository

import com.dwan.common.Either
import com.dwan.common.map
import com.dwan.common.onFailure
import com.dwan.data.source.PresidentDataSource
import com.dwan.data.source.remote.toModel
import com.dwan.domain.model.PresidentModel
import com.dwan.domain.repository.PresidentRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PresidentRepositoryImpl @Inject constructor(private val dataSource: PresidentDataSource) :
    PresidentRepository {
    override suspend fun getPresidentList(): Either<Exception, List<PresidentModel>> =
        when (val response = dataSource.getPresidentList()) {
            is Either.Left -> response.onFailure {}
            is Either.Right -> response.map { list ->
                list.map { it.toModel() }
            }
        }

    override suspend fun getPresidentDetail(id: Int): Either<Exception, PresidentModel> =
        when (val response = dataSource.getPresidentDetail(id)) {
            is Either.Left -> response.onFailure {}
            is Either.Right -> response.map {
                it.toModel()
            }
        }

    override suspend fun getPresidentBySearch(word: String): Either<Exception, List<PresidentModel>> =
        when (val response = dataSource.getPresidentBySearch(word)) {
            is Either.Left -> response.onFailure {}
            is Either.Right -> response.map { list ->
                list.map { it.toModel() }
            }
        }
}