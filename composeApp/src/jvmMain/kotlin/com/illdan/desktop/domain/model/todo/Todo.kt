package com.illdan.desktop.domain.model.todo

import com.illdan.desktop.domain.enums.TodoStatus
import kotlinx.serialization.Serializable

@Serializable
data class Todo(
    val todoId: Long = -1,
    val content: String = "",
    val todoStatus: TodoStatus = TodoStatus.INCOMPLETE,
    val isBookmark: Boolean = false,
    val isRepeat: Boolean = false,
    val isRoutine: Boolean = false,
    val dDay: Int = 0,
    val time: String = "",
    val deadline: String = "",
    val routineDays: List<String> = emptyList(),
    val categoryName: String = "",
    val imageUrl: String = ""
)
