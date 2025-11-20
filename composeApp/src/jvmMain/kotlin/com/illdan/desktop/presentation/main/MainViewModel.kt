package com.illdan.desktop.presentation.main

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.illdan.desktop.core.ui.base.BaseViewModel
import com.illdan.desktop.domain.enums.TodoStatus
import com.illdan.desktop.domain.model.category.Category
import com.illdan.desktop.domain.model.memo.Memo
import com.illdan.desktop.domain.model.request.todo.CreateTodoRequest
import com.illdan.desktop.domain.model.request.todo.GetTodoListRequest
import com.illdan.desktop.domain.model.request.todo.TodoId
import com.illdan.desktop.domain.model.today.TodayListInfo
import com.illdan.desktop.domain.model.todo.Todo
import com.illdan.desktop.domain.repository.CategoryRepository
import com.illdan.desktop.domain.repository.TodoRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class MainViewModel(
    private val todoRepository: TodoRepository,
    private val categoryRepository: CategoryRepository
): BaseViewModel<MainUiState>(MainUiState()) {
    private var logger = Logger.withTag("MainViewModel")
    private var tempId = 1L

    init {
        initCategory()
        getCategoryList()
        getTodayList()
    }

    private fun initCategory() {
        updateState(
            uiState.value.copy(
                currentCategory = uiState.value.todayCategory,
                currentTodoList = uiState.value.todayList
            )
        )
    }

    /**---------------------------------------------카테고리----------------------------------------------*/

    // 카테고리 조회
    private fun getCategoryList() {
        logger.d { "getCategoryList" }
        viewModelScope.launch {
            categoryRepository.getCategoryList().collect {
                resultResponse(it, ::onSuccessGetCategoryList)
            }
        }
    }

    private fun onSuccessGetCategoryList(result: List<Category>) {
        logger.d { "categoryList: $result" }
        updateState(uiState.value.copy(categoryList = result))
    }

    // 카테고리 선택
    fun updateCurrentCategory(index: Int) {
        val newCategory = uiState.value.categoryList[index]

        getTodoList(newCategory)

        updateState(
            uiState.value.copy(
                currentCategory = newCategory
            )
        )
    }

    /**---------------------------------------------오늘----------------------------------------------*/

    // 오늘 할 일 조회
    fun getTodayList() {
        updateState(uiState.value.copy(currentCategory = uiState.value.todayCategory))

        viewModelScope.launch {
            todoRepository.getTodayList().collect {
                resultResponse(it, ::onSuccessGetTodayList)
            }
        }
    }

    private fun onSuccessGetTodayList(result: TodayListInfo) {
        updateState(
            uiState.value.copy(
                todayList = result.todays,
                currentTodoList = result.todays
            )
        )
    }

    /**---------------------------------------------할 일 조회----------------------------------------------*/
    private fun getTodoList(category: Category) {
        viewModelScope.launch {
            todoRepository.getTodoList(request = GetTodoListRequest(categoryId = category.id)).collect {
                resultResponse(it, ::onSuccessGetTodoList)
            }
        }
    }

    private fun onSuccessGetTodoList(result: List<Todo>) {
        updateState(uiState.value.copy(currentTodoList = result))
    }

    /**---------------------------------------------할 일 생성----------------------------------------------*/
    fun createTodo(input: String) {
        viewModelScope.launch {
            val curCategoryId = uiState.value.currentCategory.id

            if (curCategoryId != -2L) {
                todoRepository.createTodo(request = CreateTodoRequest(content = input, categoryId = curCategoryId)).collect {
                    resultResponse(it, { onSuccessCreateTodo(uiState.value.currentCategory) })
                }
            }
        }
    }

    private fun onSuccessCreateTodo(category: Category) {
        getTodoList(category)
    }

    /**---------------------------------------------할 일 생성----------------------------------------------*/
    fun swipeTodo(todoId: Long) {
        val newList = uiState.value.currentTodoList.filter { it.todoId != todoId }
        updateState(uiState.value.copy(currentTodoList = newList))

        viewModelScope.launch {
            todoRepository.swipeTodo(request = TodoId(todoId)).collect {
                resultResponse(it, ::onSuccessSwipeTodo)
            }
        }
    }

    private fun onSuccessSwipeTodo(result: Unit) {
        val category = uiState.value.currentCategory

        if (category.id == -2L) {
            getTodoList(category)
        } else {
            getTodayList()
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

    /**---------------------------------------------메모----------------------------------------------*/

    // 메모 생성
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