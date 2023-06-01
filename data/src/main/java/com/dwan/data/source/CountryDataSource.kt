package com.dwan.data.source

import com.dwan.common.Either
import com.dwan.data.source.remote.CountryEntity

interface CountryDataSource {

    suspend fun getCountry(): Either<Exception, CountryEntity>
}