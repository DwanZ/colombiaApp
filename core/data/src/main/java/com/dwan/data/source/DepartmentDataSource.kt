package com.dwan.data.source

import com.dwan.data.source.remote.AttractionEntity
import com.dwan.data.source.remote.DepartmentEntity

interface DepartmentDataSource {
    suspend fun getDepartments(): Result<List<DepartmentEntity>>
    suspend fun getDepartment(id: Int): Result<DepartmentEntity>
    suspend fun getTouristicAttractions(departmentId: Int): Result<List<AttractionEntity>>
}
