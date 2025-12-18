package com.illdan.desktop.domain.model.memo

import kotlinx.serialization.Serializable

@Serializable
data class Memo(
    val noteId: Long = -1L,
    val title: String = "",
    val content: String = "",
    val modifyDate: String = ""
)
