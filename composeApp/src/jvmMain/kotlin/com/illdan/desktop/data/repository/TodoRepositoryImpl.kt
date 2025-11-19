package com.illdan.desktop.data.repository

import com.illdan.desktop.core.network.NetworkClient
import com.illdan.desktop.core.network.base.BaseRepository
import com.illdan.desktop.data.mapper.TodayListResponseMapper
import com.illdan.desktop.data.mapper.TodoListResponseMapper
import com.illdan.desktop.domain.datasource.TodoLocalDataSource
import com.illdan.desktop.domain.enums.HttpMethod
import com.illdan.desktop.domain.model.request.todo.GetTodoListRequest
import com.illdan.desktop.domain.model.today.TodayListInfo
import com.illdan.desktop.domain.model.todo.Todo
import com.illdan.desktop.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TodoRepositoryImpl(
    private val networkClient: NetworkClient,
    private val dataSource: TodoLocalDataSource
): TodoRepository, BaseRepository(networkClient) {
    override suspend fun getTodayList(): Flow<Result<TodayListInfo>> = flow {
        emit(
            fetchMapped(
                HttpMethod.GET,
                path = "/todays",
                query = mapOf(
                    "page" to "0",
                    "size" to "200"
                ),
                mapper = TodayListResponseMapper
            )
        )
    }

    override suspend fun getTodoList(request: GetTodoListRequest): Flow<Result<List<Todo>>> = flow {
        emit(
            fetchMapped(
                method = HttpMethod.GET,
                path = "/backlogs",
                query = mapOf(
                    "category" to "${request.categoryId}",
                    "page" to "0",
                    "size" to "200"
                ),
                mapper = TodoListResponseMapper
            )
        )
    }

    override suspend fun createLocalTodo(todo: Todo): Flow<Result<Unit>> = flow {
        dataSource.upsert(todo)
    }
}