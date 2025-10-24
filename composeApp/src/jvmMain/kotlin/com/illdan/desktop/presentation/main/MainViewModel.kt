package com.illdan.desktop.presentation.main

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.illdan.desktop.core.ui.base.BaseViewModel
import com.illdan.desktop.domain.enums.TodoStatus
import com.illdan.desktop.domain.model.memo.Memo
import com.illdan.desktop.domain.model.todo.Todo
import com.illdan.desktop.domain.repository.TodoRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class MainViewModel(
    private val todoRepository: TodoRepository
): BaseViewModel<MainUiState>(MainUiState()) {
    private var logger = Logger.withTag("MainViewModel")
    private var tempId = 1L

    init {
        initCategory()
    }

    private fun initCategory() {
        updateState(
            uiState.value.copy(
                currentCategory = uiState.value.categoryList.first(),
                currentTodoList = uiState.value.todayList
            )
        )
    }

    /**---------------------------------------------할 일 생성----------------------------------------------*/
    fun createTodo(input: String) {
        viewModelScope.launch {
            val newTodo = Todo(
                todoId = tempId++,
                content = input
            )
            val isToday = uiState.value.currentCategory.id == -1L

            val newTodoList  = if (isToday) uiState.value.todoList else listOf(newTodo) + uiState.value.todoList
            val newTodayList = if (isToday) listOf(newTodo) + uiState.value.todayList else uiState.value.todayList
            val newCurrent   = if (isToday) newTodayList else newTodoList

            updateState(
                uiState.value.copy(
                    todoList = newTodoList,
                    todayList = newTodayList,
                    currentTodoList = newCurrent
                )
            )
        }
    }

    /**---------------------------------------------할 일 체크----------------------------------------------*/
    fun updateTodoStatus(currentStatus: TodoStatus, id: Long) {
        val todo = uiState.value.todayList.firstOrNull { it.todoId == id }

        if (todo == null) return

        val newTodo = todo.copy(todoStatus = if (currentStatus == TodoStatus.INCOMPLETE) TodoStatus.COMPLETED else TodoStatus.INCOMPLETE)
        val newList = uiState.value.todayList.map { if (it.todoId == id) newTodo else it }

        updateState(
            uiState.value.copy(
                todayList = newList,
                currentTodoList = if (uiState.value.currentCategory.id == -1L) newList else uiState.value.currentTodoList
            )
        )
    }

    /**---------------------------------------------할 일 드래그 앤 드롭----------------------------------------------*/

    fun onMove(from: Int, to: Int) {
        val currentList = uiState.value.currentTodoList.toMutableList()
        currentList.move(from, to)
        onDragEnd(currentList)
        updateState(uiState.value.copy(currentTodoList = currentList))
    }

    fun onDragEnd(newList: List<Todo>) {
        // TODO(서버 통신 로직)
    }

    /**---------------------------------------------카테고리 선택----------------------------------------------*/
    fun updateCurrentCategory(index: Int) {
        updateState(
            uiState.value.copy(
                currentCategory = uiState.value.categoryList[index],
                currentTodoList = if (index == 0) uiState.value.todayList else uiState.value.todoList
            )
        )
    }

    /**---------------------------------------------메모 생성----------------------------------------------*/
    fun createMemo(input: Pair<String, String>) {
        val newMemo = Memo(
            id = tempId++,
            title = input.first,
            content = input.second
        )
    }

    /**---------------------------------------------기타 상태 처리----------------------------------------------*/
    fun toggleSideBarShrink() {
        updateState(uiState.value.copy(isSideBarShrink = !uiState.value.isSideBarShrink))
    }

    fun toggleMemoShrink() {
        updateState(uiState.value.copy(isMemoShrink = !uiState.value.isMemoShrink))
    }
}

fun <T> MutableList<T>.move(from: Int, to: Int) {
    if (from == to) return
    if (from !in 0 until size || to !in 0..size) return
    val element = this.removeAt(from) ?: return
    val safeToIndex = to.coerceIn(0, size)
    this.add(safeToIndex, element)
}