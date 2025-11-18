package com.illdan.desktop.data.model.response.category

import kotlinx.serialization.Serializable

@Serializable
data class CategoryListResponse(
    val categories: List<CategoryResponse>
)
