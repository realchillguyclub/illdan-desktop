package com.illdan.desktop.presentation.main

import com.illdan.desktop.core.ui.base.UiState
import com.illdan.desktop.domain.model.category.Category
import com.illdan.desktop.domain.model.memo.Memo
import com.illdan.desktop.domain.model.todo.Todo

data class MainUiState(
    val todayCategory: Category = Category(
        name = "오늘 할 일",
        id = -2,
        imageUrl = "",
        emojiId = 1
    ),
    val categoryList: List<Category> = emptyList(),
    val currentCategory: Category = Category(),
    val currentTodoList: List<Todo> = emptyList(),
    val todayList: List<Todo> = emptyList(),
    val todoList: List<Todo> = emptyList(),
    val isSideBarShrink: Boolean = true,
    val isMemoShrink: Boolean = true,
    val memoList: List<Memo> = listOf(
        Memo(
            id = 1,
            title = "첫번째 메모",
            content = "내용이요"
        ),
        Memo(
            id = 2,
            title = "두번째 메모",
            content = "내용이요"
        ),
        Memo(
            id = 3,
            title = "세번째 메모",
            content = "내용이요"
        ),
        Memo(
            id = 4,
            title = "세번째 메모",
            content = "내용이요"
        ),
        Memo(
            id = 5,
            title = "세번째 메모",
            content = "내용이요"
        ),
        Memo(
            id = 6,
            title = "세번째 메모",
            content = "내용이요"
        )
    )
): UiState
