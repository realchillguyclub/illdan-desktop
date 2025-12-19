package com.illdan.desktop.data.model.response.memo

import kotlinx.serialization.Serializable

@Serializable
data class MemoListResponse(
    val notes: List<MemoResponse>
)
