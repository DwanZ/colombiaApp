package com.dwan.data.repository

import com.dwan.common.Either
import com.dwan.common.map
import com.dwan.common.onFailure
import com.dwan.data.source.CountryDataSource
import com.dwan.data.source.remote.toModel
import com.dwan.domain.model.CountryModel
import com.dwan.domain.repository.CountryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountryRepositoryImpl @Inject constructor(private val dataSource: CountryDataSource) :
    CountryRepository {

    override suspend fun getCountry(): Either<Exception, CountryModel> {
        return when (val response = dataSource.getCountry()) {
            is Either.Left -> response.onFailure {}
            is Either.Right -> response.map { it.toModel() }
        }
    }
}