package com.illdan.desktop.presentation.main

import com.illdan.desktop.core.ui.base.UiState
import com.illdan.desktop.domain.enums.AlertType
import com.illdan.desktop.domain.model.category.Category
import com.illdan.desktop.domain.model.category.GroupEmoji
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
    val emojiList: GroupEmoji = GroupEmoji(),
    val currentCategory: Category = Category(),             // 카테고리 할 일 조회에 사용되는 변수 ex) 중요, 전체 등등
    val currentTodoList: List<Todo> = emptyList(),
    val todayList: List<Todo> = emptyList(),
    val todoList: List<Todo> = emptyList(),
    val isSideBarShrink: Boolean = true,
    val isMemoShrink: Boolean = true,
    val memoList: List<Memo> = emptyList(),
    val selectedMemo: Memo = Memo(),
    val alertType: AlertType = AlertType.None
): UiState
