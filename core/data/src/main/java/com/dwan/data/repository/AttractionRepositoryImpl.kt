package com.dwan.data.repository

import com.dwan.data.source.AttractionDataSource
import com.dwan.data.source.remote.toModel
import com.dwan.domain.model.AttractionModel
import com.dwan.domain.model.AttractionPageModel
import com.dwan.domain.repository.AttractionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AttractionRepositoryImpl @Inject constructor(
    private val dataSource: AttractionDataSource
) : AttractionRepository {

    override suspend fun getAttractionDetail(id: Int): Result<AttractionModel> =
        withContext(Dispatchers.IO) {
            dataSource.getAttractionDetail(id).mapCatching { entity ->
                val cityName = dataSource.getCityName(entity.cityId).getOrElse {
                    entity.city?.name.orEmpty()
                }
                entity.toModel(cityName)
            }
        }

    override suspend fun getAttractionByPage(
        page: String,
        limit: String
    ): Result<AttractionPageModel> = withContext(Dispatchers.IO) {
        dataSource.getAttractionByPage(page, limit).mapCatching { it.toModel() }
    }

    override suspend fun getAttractionBySearch(word: String): Result<List<AttractionModel>> =
        withContext(Dispatchers.IO) {
            dataSource.getAttractionBySearch(word).mapCatching { list ->
                list.map { it.toModel(it.city?.name.orEmpty()) }
            }
        }
}
