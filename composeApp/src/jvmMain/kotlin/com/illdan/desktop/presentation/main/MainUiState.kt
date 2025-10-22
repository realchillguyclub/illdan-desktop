package com.illdan.desktop.presentation.main

import com.illdan.desktop.core.ui.base.UiState
import com.illdan.desktop.domain.model.category.Category
import com.illdan.desktop.domain.model.todo.Todo

data class MainUiState(
    val categoryList: List<Category> = listOf(
        Category(
            name = "오늘 할 일",
            id = -1,
            imageUrl = "",
            emojiId = 1
        ),
        Category(
            name = "전체 할 일",
            id = 0,
            imageUrl = "",
            emojiId = 2
        )
    ),
    val currentCategory: Category = Category(),
    val currentTodoList: List<Todo> = emptyList(),
    val todayList: List<Todo> = emptyList(),
    val todoList: List<Todo> = emptyList()
): UiState
