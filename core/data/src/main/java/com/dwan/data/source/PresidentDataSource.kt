package com.dwan.data.source

import com.dwan.data.source.remote.PresidentEntity

interface PresidentDataSource {
    suspend fun getPresidentList(): Result<List<PresidentEntity>>
    suspend fun getPresidentDetail(id: Int): Result<PresidentEntity>
    suspend fun getPresidentBySearch(word: String): Result<List<PresidentEntity>>
}
