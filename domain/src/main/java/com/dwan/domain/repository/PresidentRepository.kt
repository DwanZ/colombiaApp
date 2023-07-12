package com.dwan.domain.repository

import com.dwan.common.Either
import com.dwan.domain.model.PresidentModel

interface PresidentRepository {

    suspend fun getPresidentList(): Either<Exception, List<PresidentModel>>

    suspend fun getPresidentDetail(id: Int): Either<Exception, PresidentModel>

    suspend fun getPresidentBySearch(word: String): Either<Exception, List<PresidentModel>>

}