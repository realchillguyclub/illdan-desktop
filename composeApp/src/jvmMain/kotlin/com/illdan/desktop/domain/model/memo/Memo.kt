package com.illdan.desktop.domain.model.memo

import kotlinx.serialization.Serializable

@Serializable
data class Memo(
    val id: Long = -1L,
    val title: String = "",
    val content: String = "",
    val createdAt: String = ""
)
