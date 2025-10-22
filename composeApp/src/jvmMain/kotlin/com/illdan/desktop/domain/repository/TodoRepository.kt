package com.illdan.desktop.domain.repository

import com.illdan.desktop.domain.model.response.TodayListInfo
import com.illdan.desktop.domain.model.todo.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    suspend fun getTodayList(): Flow<Result<TodayListInfo>>
    suspend fun createLocalTodo(todo: Todo): Flow<Result<Unit>>
}