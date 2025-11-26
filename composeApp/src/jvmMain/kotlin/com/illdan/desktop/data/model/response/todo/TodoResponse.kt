package com.illdan.desktop.data.model.response.todo

import kotlinx.serialization.Serializable

@Serializable
data class TodoResponse(
    val todoId: Long,
    val content: String,
    val isBookmark: Boolean,
    val isRepeat: Boolean,
    val isRoutine: Boolean,
    val dDay: Int?,
    val time: String?,
    val deadline: String?,
    val routineDays: List<String>?,
    val categoryName: String?,
    val imageUrl: String?
)
