package com.dwan.data.source.remote.source

import com.dwan.data.network.ColombiaApi
import com.dwan.data.source.CountryDataSource
import com.dwan.data.source.remote.CountryEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountryRemoteDataSource @Inject constructor(
    private val api: ColombiaApi
) : CountryDataSource {
    override suspend fun getCountry(): Result<CountryEntity> = runCatching { api.getCountry() }
}
