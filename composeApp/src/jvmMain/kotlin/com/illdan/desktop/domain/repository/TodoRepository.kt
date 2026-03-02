package com.illdan.desktop.domain.repository

import com.illdan.desktop.domain.model.request.todo.CreateTodoRequest
import com.illdan.desktop.domain.model.request.todo.GetTodoListRequest
import com.illdan.desktop.domain.model.request.todo.ReorderTodoListRequest
import com.illdan.desktop.domain.model.request.todo.TodoId
import com.illdan.desktop.domain.model.today.TodayListInfo
import com.illdan.desktop.domain.model.todo.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    suspend fun getTodayList(): Result<TodayListInfo>

    suspend fun getTodoList(request: GetTodoListRequest): Result<List<Todo>>

    suspend fun createTodo(request: CreateTodoRequest): Result<Unit>

    suspend fun swipeTodo(request: TodoId): Result<Unit>

    suspend fun reorderTodoList(request: ReorderTodoListRequest): Result<Unit>

    suspend fun updateTodoStatus(todoId: Long): Result<Unit>

    suspend fun updateTodoBookmark(todoId: Long): Result<Unit>

    suspend fun createLocalTodo(todo: Todo): Flow<Result<Unit>>

    suspend fun deleteTodo(todoId: Long): Result<Unit>
}
