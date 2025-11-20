package com.illdan.desktop.domain.model.request.todo

import kotlinx.serialization.Serializable

@Serializable
data class CreateTodoRequest(
    val content: String,
    val categoryId: Long
)
