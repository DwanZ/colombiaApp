package com.dwan.data.source.local

import com.dwan.data.source.remote.CategoryEntity
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader


class CategoryCache {

    var cache: Cache<Int, CategoryEntity> = CacheBuilder.newBuilder()
        .build(
            CacheLoader.from { id: Int -> getCategory(id) }
        )

    fun getCategory(id: Int): CategoryEntity? =
        cache.getIfPresent(id)

    fun saveCategory(category: CategoryEntity) = cache.put(category.id, category)
}