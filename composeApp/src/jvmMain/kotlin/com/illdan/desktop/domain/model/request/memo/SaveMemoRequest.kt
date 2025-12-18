package com.illdan.desktop.domain.model.request.memo

import kotlinx.serialization.Serializable

@Serializable
data class SaveMemoRequest(
    val title: String,
    val content: String
)
