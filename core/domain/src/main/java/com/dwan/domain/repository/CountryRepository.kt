package com.dwan.domain.repository

import com.dwan.domain.model.CountryModel

interface CountryRepository {
    suspend fun getCountry(): Result<CountryModel>
}
