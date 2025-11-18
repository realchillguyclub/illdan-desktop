package com.illdan.desktop.data.mapper

import com.illdan.desktop.core.network.base.Mapper
import com.illdan.desktop.data.model.response.category.CategoryListResponse
import com.illdan.desktop.domain.model.category.Category

object CategoryListResponseMapper: Mapper<CategoryListResponse, List<Category>> {
    override fun responseToModel(response: CategoryListResponse?): List<Category> {
        return response?.categories?.let { list ->
            list.map {
                Category(
                    id = it.id,
                    name = it.name,
                    emojiId = it.emojiId,
                    imageUrl = it.imageUrl
                )
            }
        } ?: emptyList()
    }
}