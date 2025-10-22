package com.illdan.desktop.data.local.db

import com.illdan.desktop.domain.model.todo.Todo
import com.illdan.desktop.local.db.TodoEntity

fun TodoEntity.toDomain(): Todo =
    Todo(
        todoId = todoId,
        content = content,
        todoStatus = todoStatus,
        isBookmark = isBookmark,
        isRepeat = isRepeat,
        isRoutine = isRoutine,
        dDay = dDay.toInt(),
        time = time,
        deadline = deadline,
        routineDays = routineDays,
        categoryName = categoryName,
        imageUrl = imageUrl
    )

fun Todo.toEntity(): TodoEntity =
    TodoEntity(
        todoId = todoId,
        content = content,
        todoStatus = todoStatus,
        isBookmark = isBookmark,
        isRepeat = isRepeat,
        isRoutine = isRoutine,
        dDay = dDay.toLong(),
        time = time,
        deadline = deadline,
        routineDays = routineDays,
        categoryName = categoryName,
        imageUrl = imageUrl
    )