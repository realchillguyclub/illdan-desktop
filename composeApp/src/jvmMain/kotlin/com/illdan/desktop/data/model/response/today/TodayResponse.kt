package com.illdan.desktop.data.model.response.today

import com.illdan.desktop.domain.enums.TodoStatus
import kotlinx.serialization.Serializable

@Serializable
data class TodayResponse(
    val todoId: Long,
    val content: String,
    val todayStatus: TodoStatus,
    val isBookmark: Boolean,
    val isRepeat: Boolean,
    val isRoutine: Boolean,
    val dDay: Int,
    val time: String,
    val deadline: String,
    val routineDays: List<String>,
    val categoryName: String,
    val imageUrl: String
)
