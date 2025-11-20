package com.illdan.desktop.domain.repository

import com.illdan.desktop.domain.model.request.todo.CreateTodoRequest
import com.illdan.desktop.domain.model.request.todo.GetTodoListRequest
import com.illdan.desktop.domain.model.today.TodayListInfo
import com.illdan.desktop.domain.model.todo.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    suspend fun getTodayList(): Flow<Result<TodayListInfo>>
    suspend fun getTodoList(request: GetTodoListRequest): Flow<Result<List<Todo>>>
    suspend fun createTodo(request: CreateTodoRequest): Flow<Result<Unit>>
    suspend fun createLocalTodo(todo: Todo): Flow<Result<Unit>>
}