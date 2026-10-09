package com.dwan.data.repository

import com.dwan.data.source.AttractionDataSource
import com.dwan.data.source.DepartmentDataSource
import com.dwan.data.source.remote.toModel
import com.dwan.domain.model.AttractionModel
import com.dwan.domain.model.DepartmentModel
import com.dwan.domain.repository.DepartmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DepartmentRepositoryImpl @Inject constructor(
    private val dataSource: DepartmentDataSource,
    private val attractionDataSource: AttractionDataSource
) : DepartmentRepository {

    override suspend fun getDepartments(): Result<List<DepartmentModel>> =
        withContext(Dispatchers.IO) {
            dataSource.getDepartments().mapCatching { list -> list.map { it.toModel() } }
        }

    override suspend fun getDepartment(id: Int): Result<DepartmentModel> =
        withContext(Dispatchers.IO) {
            dataSource.getDepartment(id).mapCatching { it.toModel() }
        }

    override suspend fun getTouristicAttractions(departmentId: Int): Result<List<AttractionModel>> =
        withContext(Dispatchers.IO) {
            val direct = dataSource.getTouristicAttractions(departmentId)
            if (direct.isSuccess) {
                return@withContext direct.mapCatching { list ->
                    list.map { it.toModel() }
                }
            }

            // Many departments return 404 on the department endpoint; fall back to full list filter.
            attractionDataSource.getAttractionList().mapCatching { list ->
                list.map { it.toModel() }
                    .filter { it.departmentId == departmentId }
            }
        }
}
