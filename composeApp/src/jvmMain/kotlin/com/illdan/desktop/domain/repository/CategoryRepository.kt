package com.illdan.desktop.domain.repository

import com.illdan.desktop.domain.model.category.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun getCategoryList(): Flow<Result<List<Category>>>
}