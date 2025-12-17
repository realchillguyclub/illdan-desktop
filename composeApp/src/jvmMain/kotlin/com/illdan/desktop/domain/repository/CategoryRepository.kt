package com.illdan.desktop.domain.repository

import com.illdan.desktop.domain.model.category.Category
import com.illdan.desktop.domain.model.category.GroupEmoji
import com.illdan.desktop.domain.model.request.category.CreateCategoryRequest
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun getCategoryList(): Flow<Result<List<Category>>>
    suspend fun getEmojiList(): Flow<Result<GroupEmoji>>
    suspend fun createCategory(request: CreateCategoryRequest): Flow<Result<Unit>>
}