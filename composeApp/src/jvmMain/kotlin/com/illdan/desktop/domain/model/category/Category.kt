package com.illdan.desktop.domain.model.category

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Long = -1,
    val name: String = "",
    val emojiId: Long = -1,
    val imageUrl: String = ""
)
