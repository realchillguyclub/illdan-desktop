package com.illdan.desktop.domain.model.request.category

import kotlinx.serialization.Serializable

@Serializable
data class CreateCategoryRequest(
    val name: String,
    val emojiId: Long
)
