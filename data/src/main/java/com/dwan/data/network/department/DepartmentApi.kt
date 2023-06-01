package com.dwan.data.network.department

import com.dwan.data.source.remote.DepartmentEntity
import retrofit2.http.GET

interface DepartmentApi {
    @GET("departament")
    suspend fun getDepartments(): List<DepartmentEntity>
}