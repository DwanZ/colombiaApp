package com.dwan.data.repository

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
    private val dataSource: DepartmentDataSource
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
            dataSource.getTouristicAttractions(departmentId).mapCatching { list ->
                list.map { it.toModel(it.city?.name.orEmpty()) }
            }
        }
}
