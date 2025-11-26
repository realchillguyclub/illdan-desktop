package com.illdan.desktop.data.local.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.illdan.desktop.AppDatabase
import com.illdan.desktop.data.local.db.toDomain
import com.illdan.desktop.domain.datasource.TodoLocalDataSource
import com.illdan.desktop.domain.model.todo.Todo
import com.illdan.desktop.local.db.TodoEntityQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TodoLocalDataSourceImpl(
    private val db: AppDatabase
): TodoLocalDataSource {
    private val q = db.todoEntityQueries

    override fun observeList(): Flow<List<Todo>> =
        q.getAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toDomain() } }


    override fun observeById(id: Long): Flow<Todo?> =
        q.getById(id)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .map { entity ->  entity?.toDomain() }

    override suspend fun getByIdOnce(id: Long): Todo? =
        withContext(Dispatchers.IO) {
            q.getById(id).executeAsOneOrNull()?.toDomain()
        }

    override suspend fun upsertAll(items: List<Todo>) = withContext(Dispatchers.IO) {
        db.transaction {
            for (item in items) {
                q.upsertTodo(item)
            }
        }
    }

    override suspend fun upsert(item: Todo) = withContext(Dispatchers.IO) {
        q.upsertTodo(item)
    }

    private fun TodoEntityQueries.upsertTodo(item: Todo) {
        upsert(
            todoId = item.todoId,
            content = item.content,
            todoStatus = item.todoStatus,
            isBookmark = item.isBookmark,
            isRepeat = item.isRepeat,
            isRoutine = item.isRoutine,
            dDay = item.dDay.toLong(),
            time = item.time,
            deadline = item.deadline,
            routineDays = item.routineDays,
            categoryName = item.categoryName,
            imageUrl = item.imageUrl
        )
    }

    override suspend fun deleteById(id: Long) {
        withContext(Dispatchers.IO) {
            q.deleteById(id)
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            q.clear()
        }
    }
}