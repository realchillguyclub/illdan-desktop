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

class CategoryRepositoryImpl(
    private val networkClient: NetworkClient,
) : BaseRepository(networkClient),
    CategoryRepository {
    override suspend fun getCategoryList(): Result<List<Category>> =
        fetchMapped(
            method = HttpMethod.GET,
            path = "/category/list",
            query = mapOf("page" to "0", "size" to "100"),
            mapper = CategoryListResponseMapper,
        )

    override suspend fun getEmojiList(): Result<GroupEmoji> =
        fetchMapped(
            method = HttpMethod.GET,
            path = "/emojis",
            query = mapOf("page" to "0", "size" to "100"),
            mapper = GroupEmojiResponseMapper,
        )

    override suspend fun createCategory(request: CreateCategoryRequest): Result<Unit> =
        fetch(
            method = HttpMethod.POST,
            path = "/category",
            body = request,
        )

    override suspend fun updateCategory(
        id: Long,
        name: String,
        emojiId: Long,
    ): Result<Unit> =
        fetch(
            method = HttpMethod.PUT,
            path = "/category/$id",
            body = CreateCategoryRequest(name, emojiId),
        )
}
