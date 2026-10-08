package com.dwan.domain.repository

import com.dwan.domain.model.PresidentModel

interface PresidentRepository {
    suspend fun getPresidentList(): Result<List<PresidentModel>>
    suspend fun getPresidentDetail(id: Int): Result<PresidentModel>
    suspend fun getPresidentBySearch(word: String): Result<List<PresidentModel>>
}
