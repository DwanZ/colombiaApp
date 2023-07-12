package com.dwan.data.source

import com.dwan.common.Either
import com.dwan.data.source.remote.PresidentEntity

interface PresidentDataSource {

    suspend fun getPresidentList(): Either<Exception, List<PresidentEntity>>

    suspend fun getPresidentDetail(id: Int): Either<Exception, PresidentEntity>

    suspend fun getPresidentBySearch(word: String): Either<Exception, List<PresidentEntity>>
}