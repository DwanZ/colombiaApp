package com.dwan.domain.repository

import com.dwan.common.Either
import com.dwan.domain.model.CountryModel

interface CountryRepository {

    suspend fun getCountry() : Either<Exception, CountryModel>
}