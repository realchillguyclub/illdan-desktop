package com.illdan.desktop.domain.repository

import com.illdan.desktop.domain.model.category.Category
import com.illdan.desktop.domain.model.category.GroupEmoji
import com.illdan.desktop.domain.model.request.category.CreateCategoryRequest

interface CategoryRepository {
    suspend fun getCategoryList(): Result<List<Category>>

    suspend fun getEmojiList(): Result<GroupEmoji>

    suspend fun createCategory(request: CreateCategoryRequest): Result<Unit>

    suspend fun updateCategory(
        id: Long,
        name: String,
        emojiId: Long,
    ): Result<Unit>

    suspend fun deleteCategory(id: Long): Result<Unit>
}
