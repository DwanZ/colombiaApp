package com.dwan.data.source.remote.source

import com.dwan.common.Either
import com.dwan.data.network.ColombiaApi
import com.dwan.data.source.CountryDataSource
import com.dwan.data.source.remote.CountryEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountryRemoteDataSource @Inject constructor(private val api: ColombiaApi) :
    CountryDataSource {

    override suspend fun getCountry(): Either<Exception, CountryEntity> =
        try {
            val response = api.getCountry()
            Either.Right(response)
        } catch (e: Exception) {
            Either.Left(e)
        }
}