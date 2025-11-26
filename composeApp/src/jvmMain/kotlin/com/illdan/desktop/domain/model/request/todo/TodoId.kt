package com.illdan.desktop.domain.model.request.todo

import kotlinx.serialization.Serializable

@Serializable
data class TodoId(
    val todoId: Long
)
