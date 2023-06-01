package com.dwan.data.repository

import com.dwan.common.Either
import com.dwan.data.source.CountryDataSource
import com.dwan.data.source.remote.toModel
import com.dwan.domain.model.CountryModel
import com.dwan.domain.repository.CountryRepository
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(private val dataSource: CountryDataSource) :
    CountryRepository {

    override suspend fun getCountry(): Either<Exception, CountryModel> {
        return when (val data = dataSource.getCountry()) {
            is Either.Left -> {
                data
            }
            is Either.Right -> {
                Either.Right(data.b.toModel())
            }
        }
    }
}