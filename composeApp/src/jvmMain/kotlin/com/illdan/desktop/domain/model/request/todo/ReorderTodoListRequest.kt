package com.illdan.desktop.domain.model.request.todo

import kotlinx.serialization.Serializable

@Serializable
data class ReorderTodoListRequest(
    val type: String,
    val todoIds: List<Long>
)
