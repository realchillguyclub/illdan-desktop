package com.illdan.desktop.data.repository

import com.illdan.desktop.core.network.NetworkClient
import com.illdan.desktop.core.network.base.BaseRepository
import com.illdan.desktop.data.mapper.CategoryListResponseMapper
import com.illdan.desktop.data.mapper.GroupEmojiResponseMapper
import com.illdan.desktop.domain.enums.HttpMethod
import com.illdan.desktop.domain.model.category.Category
import com.illdan.desktop.domain.model.category.GroupEmoji
import com.illdan.desktop.domain.model.request.category.CreateCategoryRequest
import com.illdan.desktop.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CategoryRepositoryImpl(
    private val networkClient: NetworkClient
): CategoryRepository, BaseRepository(networkClient) {
    override suspend fun getCategoryList(): Flow<Result<List<Category>>> = flow {
        emit(
            fetchMapped(
                method = HttpMethod.GET,
                path = "/category/list",
                query = mapOf("page" to "0", "size" to "100"),
                mapper = CategoryListResponseMapper
            )
        )
    }

    override suspend fun getEmojiList(): Flow<Result<GroupEmoji>> = flow {
        emit(
            fetchMapped(
                method = HttpMethod.GET,
                path = "/emojis",
                query = mapOf("page" to "0", "size" to "100"),
                mapper = GroupEmojiResponseMapper
            )
        )
    }

    override suspend fun createCategory(request: CreateCategoryRequest): Flow<Result<Unit>> = flow {
        emit(
            fetch(
                method = HttpMethod.POST,
                path = "/category",
                body = request
            )
        )
    }
}