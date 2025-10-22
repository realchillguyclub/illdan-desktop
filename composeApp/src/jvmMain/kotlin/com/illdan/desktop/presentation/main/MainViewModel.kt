package com.illdan.desktop.presentation.main

import androidx.lifecycle.viewModelScope
import com.illdan.desktop.core.ui.base.BaseViewModel
import com.illdan.desktop.domain.model.todo.Todo
import com.illdan.desktop.domain.repository.TodoRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val todoRepository: TodoRepository
): BaseViewModel<MainUiState>(MainUiState()) {
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
}

fun <T> MutableList<T>.move(from: Int, to: Int) {
    if (from == to) return
    if (from !in 0 until size || to !in 0..size) return
    val element = this.removeAt(from) ?: return
    val safeToIndex = to.coerceIn(0, size)
    this.add(safeToIndex, element)
}