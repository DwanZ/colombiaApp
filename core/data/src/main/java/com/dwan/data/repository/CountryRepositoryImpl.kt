package com.dwan.data.repository

import com.dwan.data.source.CountryDataSource
import com.dwan.data.source.remote.toModel
import com.dwan.domain.model.CountryModel
import com.dwan.domain.repository.CountryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountryRepositoryImpl @Inject constructor(
    private val dataSource: CountryDataSource
) : CountryRepository {
    override suspend fun getCountry(): Result<CountryModel> = withContext(Dispatchers.IO) {
        dataSource.getCountry().mapCatching { it.toModel() }
    }
}
