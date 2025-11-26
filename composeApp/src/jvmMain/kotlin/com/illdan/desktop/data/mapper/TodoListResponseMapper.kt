package com.illdan.desktop.data.mapper

import com.illdan.desktop.core.network.base.Mapper
import com.illdan.desktop.data.model.response.todo.TodoListResponse
import com.illdan.desktop.domain.model.todo.Todo

object TodoListResponseMapper: Mapper<TodoListResponse, List<Todo>> {
    override fun responseToModel(response: TodoListResponse?): List<Todo> {
        return response?.backlogs?.let { list ->
            list.map {
                Todo(
                    todoId = it.todoId,
                    content = it.content,
                    isBookmark = it.isBookmark,
                    isRepeat = it.isRepeat,
                    isRoutine = it.isRoutine,
                    dDay = it.dDay ?: -1,
                    deadline = it.deadline ?: "",
                    routineDays = it.routineDays ?: emptyList(),
                    categoryName = it.categoryName ?: "",
                    imageUrl = it.imageUrl ?: ""
                )
            }
        } ?: emptyList()
    }
}