package com.dwan.data.source.remote.source

import com.dwan.common.Either
import com.dwan.data.network.ColombiaApi
import com.dwan.data.source.PresidentDataSource
import com.dwan.data.source.remote.PresidentEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PresidentRemoteDataSource @Inject constructor(private val api: ColombiaApi) :
    PresidentDataSource {

    override suspend fun getPresidentList(): Either<Exception, List<PresidentEntity>> {
        return try {
            val response = api.getPresidentList()
            Either.Right(response)
        } catch (e: Exception) {
            Either.Left(e)
        }
    }

    override suspend fun getPresidentDetail(id: Int): Either<Exception, PresidentEntity> {
        return try {
            val response = api.getPresidentDetail(id)
            Either.Right(response)
        } catch (e: Exception) {
            Either.Left(e)
        }
    }

    override suspend fun getPresidentBySearch(word: String): Either<Exception, List<PresidentEntity>> {
        return try {
            val response = api.getPresidentBySearch(word)
            Either.Right(response)
        } catch (e: Exception) {
            Either.Left(e)
        }
    }
}