package com.illdan.desktop.data.mapper

import com.illdan.desktop.core.network.base.Mapper
import com.illdan.desktop.data.model.response.today.TodayResponse
import com.illdan.desktop.domain.model.todo.Todo

object TodayResponseMapper: Mapper<TodayResponse, Todo> {
    override fun responseToModel(response: TodayResponse?): Todo {
        return response?.let {
            Todo(
                todoId = it.todoId,
                content = it.content,
                todoStatus = it.todayStatus,
                isBookmark = it.isBookmark,
                isRepeat = it.isRepeat,
                isRoutine = it.isRoutine,
                dDay = it.dDay ?: 0,
                time = it.time ?: "",
                deadline = it.deadline ?: "",
                routineDays = it.routineDays ?: emptyList(),
                categoryName = it.categoryName ?: "",
                imageUrl = it.imageUrl ?: ""
            )
        } ?: Todo()
    }
}