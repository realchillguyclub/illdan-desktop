package com.illdan.desktop.data.model.response.category

import kotlinx.serialization.Serializable

@Serializable
data class EmojiResponse(
    val emojiId: Long,
    val imageUrl: String
)
