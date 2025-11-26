package com.illdan.desktop.domain.datasource

import com.illdan.desktop.domain.model.todo.Todo
import kotlinx.coroutines.flow.Flow

interface TodoLocalDataSource {
    fun observeList(): Flow<List<Todo>>                // 전체 조회
    fun observeById(id: Long): Flow<Todo?>             // 단일 조회
    suspend fun getByIdOnce(id: Long): Todo?           // 단발 조회
    suspend fun upsertAll(items: List<Todo>)
    suspend fun upsert(item: Todo)
    suspend fun deleteById(id: Long)
    suspend fun clear()
}