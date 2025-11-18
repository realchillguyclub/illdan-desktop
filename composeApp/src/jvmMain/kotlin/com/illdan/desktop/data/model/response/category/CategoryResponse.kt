package com.illdan.desktop.data.model.response.category

import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(
    val id: Long,
    val name: String,
    val emojiId: Long,
    val imageUrl: String
)
