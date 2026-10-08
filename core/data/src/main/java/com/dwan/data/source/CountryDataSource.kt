package com.dwan.data.source

import com.dwan.data.source.remote.CountryEntity

interface CountryDataSource {
    suspend fun getCountry(): Result<CountryEntity>
}
