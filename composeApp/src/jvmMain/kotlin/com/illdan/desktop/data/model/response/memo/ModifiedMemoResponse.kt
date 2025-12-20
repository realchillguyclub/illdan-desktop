package com.illdan.desktop.data.model.response.memo

import kotlinx.serialization.Serializable

@Serializable
data class ModifiedMemoResponse(
    val noteId: Long,
    val modifyDate: String
)
