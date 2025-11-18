package com.illdan.desktop.data.mapper

import com.illdan.desktop.core.network.base.Mapper
import com.illdan.desktop.data.model.response.category.CategoryResponse
import com.illdan.desktop.domain.model.category.Category

object CategoryListResponseMapper: Mapper<List<CategoryResponse>, List<Category>> {
    override fun responseToModel(response: List<CategoryResponse>?): List<Category> {
        return response?.let { list ->
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