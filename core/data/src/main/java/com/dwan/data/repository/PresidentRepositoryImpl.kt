package com.dwan.data.repository

import com.dwan.data.source.PresidentDataSource
import com.dwan.data.source.remote.toModel
import com.dwan.domain.model.PresidentModel
import com.dwan.domain.repository.PresidentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PresidentRepositoryImpl @Inject constructor(
    private val dataSource: PresidentDataSource
) : PresidentRepository {

    override suspend fun getPresidentList(): Result<List<PresidentModel>> =
        withContext(Dispatchers.IO) {
            dataSource.getPresidentList().mapCatching { list -> list.map { it.toModel() } }
        }

    override suspend fun getPresidentDetail(id: Int): Result<PresidentModel> =
        withContext(Dispatchers.IO) {
            dataSource.getPresidentDetail(id).mapCatching { it.toModel() }
        }

    override suspend fun getPresidentBySearch(word: String): Result<List<PresidentModel>> =
        withContext(Dispatchers.IO) {
            dataSource.getPresidentBySearch(word).mapCatching { list -> list.map { it.toModel() } }
        }
}
