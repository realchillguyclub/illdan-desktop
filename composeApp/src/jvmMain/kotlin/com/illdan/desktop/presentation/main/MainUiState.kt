package com.illdan.desktop.presentation.main

import com.illdan.desktop.core.design_system.components.CategoryListItem
import com.illdan.desktop.core.ui.base.UiState
import com.illdan.desktop.domain.enums.TodoStatus
import com.illdan.desktop.domain.model.category.Category
import com.illdan.desktop.domain.model.todo.Todo

data class MainUiState(
    val categoryList: List<Category> = listOf(
        Category(
            name = "오늘 할 일",
            id = 1,
            imageUrl = "",
            emojiId = 1
        )
    ),
    val currentCategory: Category = Category(),
    val todoList: List<Todo> = listOf(
        Todo(
            todoId = 1,
            content = "새로운 일정1",
            todoStatus = TodoStatus.INCOMPLETE,
            isBookmark = true,
            isRepeat = false,
            isRoutine = false,
        ),
        Todo(
            todoId = 2,
            content = "새로운 일정2",
            todoStatus = TodoStatus.COMPLETED,
            isBookmark = false,
            isRepeat = false,
            isRoutine = false,
        )
    )
): UiState
