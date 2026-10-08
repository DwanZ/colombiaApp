package com.dwan.domain.repository

import com.dwan.domain.model.AttractionModel
import com.dwan.domain.model.DepartmentModel

interface DepartmentRepository {
    suspend fun getDepartments(): Result<List<DepartmentModel>>
    suspend fun getDepartment(id: Int): Result<DepartmentModel>
    suspend fun getTouristicAttractions(departmentId: Int): Result<List<AttractionModel>>
}
