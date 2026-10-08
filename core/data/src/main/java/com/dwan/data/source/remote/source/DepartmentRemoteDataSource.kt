package com.dwan.data.source.remote.source

import com.dwan.data.network.ColombiaApi
import com.dwan.data.source.DepartmentDataSource
import com.dwan.data.source.remote.AttractionEntity
import com.dwan.data.source.remote.DepartmentEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DepartmentRemoteDataSource @Inject constructor(
    private val api: ColombiaApi
) : DepartmentDataSource {

    override suspend fun getDepartments(): Result<List<DepartmentEntity>> =
        runCatching { api.getDepartments() }

    override suspend fun getDepartment(id: Int): Result<DepartmentEntity> =
        runCatching { api.getDepartment(id) }

    override suspend fun getTouristicAttractions(departmentId: Int): Result<List<AttractionEntity>> =
        runCatching { api.getDepartmentTouristicAttractions(departmentId) }
}
