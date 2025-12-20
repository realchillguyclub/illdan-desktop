package com.illdan.desktop.data.model.response.memo

import kotlinx.serialization.Serializable

@Serializable
data class MemoResponse(
    val noteId: Long,
    val title: String?,
    val content: String?,
    val modifyDate: String
)
