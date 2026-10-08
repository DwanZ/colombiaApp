package com.dwan.data.source.remote.source

import com.dwan.data.network.ColombiaApi
import com.dwan.data.source.PresidentDataSource
import com.dwan.data.source.remote.PresidentEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PresidentRemoteDataSource @Inject constructor(
    private val api: ColombiaApi
) : PresidentDataSource {

    override suspend fun getPresidentList(): Result<List<PresidentEntity>> =
        runCatching { api.getPresidentList() }

    override suspend fun getPresidentDetail(id: Int): Result<PresidentEntity> =
        runCatching { api.getPresidentDetail(id) }

    override suspend fun getPresidentBySearch(word: String): Result<List<PresidentEntity>> =
        runCatching { api.getPresidentBySearch(word) }
}
