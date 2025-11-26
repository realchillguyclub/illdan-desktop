package com.illdan.desktop.data.model.response.todo

import kotlinx.serialization.Serializable

@Serializable
data class TodoListResponse(
    val backlogs: List<TodoResponse>
)
