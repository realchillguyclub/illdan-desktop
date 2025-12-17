package com.illdan.desktop.data.model.response.category

import kotlinx.serialization.Serializable

@Serializable
data class GroupEmojiResponse(
    val groupEmojis: Map<String, List<EmojiResponse>>
)
