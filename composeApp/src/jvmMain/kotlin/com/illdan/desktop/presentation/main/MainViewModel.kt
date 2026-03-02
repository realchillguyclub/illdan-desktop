package com.illdan.desktop.presentation.main

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.illdan.desktop.core.ui.base.BaseViewModel
import com.illdan.desktop.domain.enums.TodoStatus
import com.illdan.desktop.domain.enums.TodoType
import com.illdan.desktop.domain.model.category.Category
import com.illdan.desktop.domain.model.memo.Memo
import com.illdan.desktop.domain.model.memo.MemoId
import com.illdan.desktop.domain.model.request.category.CreateCategoryRequest
import com.illdan.desktop.domain.model.request.memo.SaveMemoRequest
import com.illdan.desktop.domain.model.request.todo.CreateTodoRequest
import com.illdan.desktop.domain.model.request.todo.GetTodoListRequest
import com.illdan.desktop.domain.model.request.todo.ReorderTodoListRequest
import com.illdan.desktop.domain.model.request.todo.TodoId
import com.illdan.desktop.domain.model.todo.Todo
import com.illdan.desktop.domain.repository.AuthRepository
import com.illdan.desktop.domain.repository.CategoryRepository
import com.illdan.desktop.domain.repository.MemoRepository
import com.illdan.desktop.domain.repository.TodoRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class MainViewModel(
    private val authRepository: AuthRepository,
    private val todoRepository: TodoRepository,
    private val categoryRepository: CategoryRepository,
    private val memoRepository: MemoRepository,
) : BaseViewModel<MainUiState>(MainUiState()) {
    private var logger = Logger.withTag("MainViewModel")
    private val emptyMemo = Memo()
    private val saveJobs = mutableMapOf<Long, Job>()

    init {
        checkForFirstOpen()
        initCategory()
        getCategoryList()
        getTodayList()
        getEmojis()
    }

    private fun checkForFirstOpen() {
        viewModelScope.launch {
            val token = authRepository.getLocalTokenOnce()

            if (token == null) emitEventFlow(MainEvent.NavigateToLogin)
        }
    }

    private fun initCategory() {
        updateState(
            uiState.value.copy(
                currentCategory = uiState.value.todayCategory,
                currentTodoList = uiState.value.todayList,
            ),
        )
    }

    // ---------------------------------------------카테고리----------------------------------------------

    // 카테고리 조회
    private fun getCategoryList() {
        logger.d { "getCategoryList" }
        launchResult(
            block = { categoryRepository.getCategoryList() },
            onSuccess = { updateState(uiState.value.copy(categoryList = it)) },
        )
    }

    // 카테고리 선택
    fun updateCurrentCategory(index: Int) {
        val newCategory = uiState.value.categoryList[index]

        getTodoList(newCategory)

        updateState(
            uiState.value.copy(
                currentCategory = newCategory,
            ),
        )
    }

    // 카테고리 이모지 조회
    fun getEmojis() {
        if (uiState.value.emojiList.groupEmojis
                .isNotEmpty()
        ) {
            return
        }

        launchResult(
            block = { categoryRepository.getEmojiList() },
            onSuccess = { updateState(uiState.value.copy(emojiList = it)) },
        )
    }

    // 카테고리 생성
    fun createCategory(
        name: String,
        emojiId: Long,
    ) {
        launchResult(
            block = { categoryRepository.createCategory(request = CreateCategoryRequest(name, emojiId)) },
            onSuccess = { getCategoryList() },
        )
    }

    // ---------------------------------------------오늘----------------------------------------------

    // 오늘 할 일 조회
    fun getTodayList() {
        updateState(uiState.value.copy(currentCategory = uiState.value.todayCategory))

        launchResult(
            block = { todoRepository.getTodayList() },
            onSuccess = { updateState(uiState.value.copy(todayList = it.todays, currentTodoList = it.todays)) },
        )
    }

    /**---------------------------------------------할 일 조회----------------------------------------------*/
    private fun getTodoList(category: Category) {
        launchResult(
            block = { todoRepository.getTodoList(request = GetTodoListRequest(categoryId = category.id)) },
            onSuccess = { updateState(uiState.value.copy(currentTodoList = it)) },
        )
    }

    /**---------------------------------------------할 일 생성----------------------------------------------*/
    fun createTodo(input: String) {
        val curCategoryId = currentState.currentCategory.id

        if (curCategoryId == -2L) return

        launchResult(
            block = { todoRepository.createTodo(request = CreateTodoRequest(content = input, categoryId = curCategoryId)) },
            onSuccess = { getTodoList(currentState.currentCategory) },
        )
    }

    /**---------------------------------------------할 일 스와이프----------------------------------------------*/
    fun swipeTodo(todoId: Long) {
        val newList = currentState.currentTodoList.filter { it.todoId != todoId }
        updateState(currentState.copy(currentTodoList = newList))

        launchResult(
            block = { todoRepository.swipeTodo(request = TodoId(todoId)) },
            onSuccess = { onSuccessSwipeTodo(it) },
        )
    }

    private fun onSuccessSwipeTodo(result: Unit) {
        val category = currentState.currentCategory

        if (category.id == -2L) {
            getTodayList()
        } else {
            getTodoList(category)
        }
    }

    /**---------------------------------------------할 일 체크----------------------------------------------*/
    fun updateTodoStatus(
        currentStatus: TodoStatus,
        id: Long,
    ) {
        val todo = uiState.value.todayList.firstOrNull { it.todoId == id }

        if (todo == null) return

        val newTodo = todo.copy(todoStatus = if (currentStatus == TodoStatus.INCOMPLETE) TodoStatus.COMPLETED else TodoStatus.INCOMPLETE)
        val remainingItems = uiState.value.todayList.filter { it.todoId != id }
        val incompleteItems = remainingItems.filter { it.todoStatus == TodoStatus.INCOMPLETE }.toMutableList()
        val completeItems = remainingItems.filter { it.todoStatus == TodoStatus.COMPLETED }.toMutableList()

        if (newTodo.todoStatus == TodoStatus.COMPLETED) {
            completeItems.add(newTodo)
        } else {
            incompleteItems.add(newTodo)
        }

        val newList = incompleteItems + completeItems

        updateState(
            uiState.value.copy(
                todayList = newList,
                currentTodoList = if (uiState.value.currentCategory.id == -2L) newList else uiState.value.currentTodoList,
            ),
        )

        launchResult(
            block = { todoRepository.updateTodoStatus(todoId = id) },
            onSuccess = {},
            onError = { getTodayList() },
        )
    }

    /**---------------------------------------------할 일 북마크----------------------------------------------*/

    fun updateTodoBookmark(todoId: Long) {
        updateTodoBookmarkInUI(todoId)

        launchResult(
            block = { todoRepository.updateTodoBookmark(todoId = todoId) },
            onSuccess = {},
            onError = { getTodoList(category = currentState.currentCategory) },
        )
    }

    private fun updateTodoBookmarkInUI(todoId: Long) {
        val todo = uiState.value.currentTodoList.firstOrNull { it.todoId == todoId }

        if (todo == null) return

        val newTodo = todo.copy(isBookmark = !todo.isBookmark)
        val newList = uiState.value.currentTodoList.map { if (it.todoId == todoId) newTodo else it }

        updateState(uiState.value.copy(currentTodoList = newList))
    }

    /**---------------------------------------------할 일 드래그 앤 드롭----------------------------------------------*/

    fun onMove(
        from: Int,
        to: Int,
    ) {
        val currentList = uiState.value.currentTodoList.toMutableList()

        if (currentList[from].todoStatus == TodoStatus.COMPLETED || currentList[to].todoStatus == TodoStatus.COMPLETED) return

        currentList.move(from, to)
        onDragEnd(currentList)
        updateState(uiState.value.copy(currentTodoList = currentList))
    }

    fun onDragEnd(newList: List<Todo>) {
        val todoIdList = newList.map { it.todoId }
        val todoType = if (uiState.value.currentCategory.id == -2L) TodoType.TODAY else TodoType.BACKLOG

        launchResult(
            block = { todoRepository.reorderTodoList(request = ReorderTodoListRequest(type = todoType.name, todoIds = todoIdList)) },
            onSuccess = {},
            onError = { onFailureReorderTodoList(todoType) },
        )
    }

    private fun onFailureReorderTodoList(todoType: TodoType) {
        when (todoType) {
            TodoType.TODAY -> getTodayList()
            TodoType.BACKLOG -> getTodoList(uiState.value.currentCategory)
        }
    }

    /**---------------------------------------------할 일 삭제----------------------------------------------*/

    fun deleteTodo(id: Long) {
        deleteTodoInUI(id)

        launchResult(
            block = { todoRepository.deleteTodo(id) },
            onSuccess = { logger.d { "할 일 삭제 성공" } },
        )
    }

    private fun deleteTodoInUI(id: Long) {
        val newList = uiState.value.currentTodoList.filter { it.todoId != id }

        updateStateSync(uiState.value.copy(currentTodoList = newList))
    }

    // ---------------------------------------------메모----------------------------------------------

    // 메모 생성
    fun createMemo() {
        launchResult(
            block = { memoRepository.saveMemo(request = SaveMemoRequest("", "")) },
            onSuccess = { onSuccessCreateMemo(it) },
        )
    }

    private fun onSuccessCreateMemo(result: MemoId) {
        val curList = currentState.memoList.toMutableList()
        val newMemo = Memo(noteId = result.memoId, title = "", content = "")
        curList.add(0, newMemo)

        updateStateSync(currentState.copy(memoList = curList, selectedMemo = newMemo))
    }

    // 메모 저장
    fun saveMemo(
        id: Long,
        input: Pair<String, String>,
    ) {
        val memo = currentState.memoList.firstOrNull { it.noteId == id }

        if (memo == null) {
            logger.d { "수정할 메모를 찾지 못했습니다. id: $id" }
            return
        }

        val updatedMemo = memo.copy(title = input.first, content = input.second)

        updateMemoInUi(updatedMemo)

        // 서버 저장은 debounce
        saveJobs[id]?.cancel()
        saveJobs[id] =
            viewModelScope.launch {
                delay(400) // debounce window
                val response = memoRepository.updateMemo(updatedMemo.noteId, SaveMemoRequest(updatedMemo.title, updatedMemo.content))
                response.fold(
                    onSuccess = { onSuccessSaveMemo(it) },
                    onFailure = { t ->
                        if (t is CancellationException) throw t
                        logger.e { "메모 저장 실패(noteId=${updatedMemo.noteId}): ${t.stackTraceToString()}" }
                    },
                )
            }
    }

    private fun onSuccessSaveMemo(result: Pair<Long, String>) {
        val updatedList =
            uiState.value.memoList.map {
                if (it.noteId == result.first) it.copy(modifyDate = result.second) else it
            }

        updateStateSync(uiState.value.copy(memoList = updatedList))
    }

    private fun updateMemoInUi(updatedMemo: Memo) {
        val cur = uiState.value.memoList.toMutableList()
        val index = cur.indexOfFirst { it.noteId == updatedMemo.noteId }
        if (index == -1) return

        cur[index] = updatedMemo

        updateState(uiState.value.copy(memoList = cur))
    }

    // 선택된 메모 업데이트
    fun updateSelectedMemo(id: Long) {
        val memo = uiState.value.memoList.firstOrNull { it.noteId == id }

        if (memo == null) {
            updateState(uiState.value.copy(selectedMemo = emptyMemo))
        } else {
            updateState(uiState.value.copy(selectedMemo = memo))
        }
    }

    // 메모 리스트 조회
    fun getMemoList() {
        launchResult(
            block = { memoRepository.getMemoList() },
            onSuccess = { updateState(uiState.value.copy(memoList = it)) },
        )
    }

    // 메모 삭제
    fun deleteMemo(memoId: Long) {
        deleteMemoInUI(memoId)

        launchResult(
            block = { memoRepository.deleteMemo(memoId) },
            onSuccess = {},
            onError = ::onFailureDeleteMemo,
        )
    }

    private fun deleteMemoInUI(memoId: Long) {
        val curList = uiState.value.memoList.toMutableList()
        val index = curList.indexOfFirst { it.noteId == memoId }

        if (index == -1) return

        curList.removeAt(index)

        updateStateSync(uiState.value.copy(memoList = curList, selectedMemo = emptyMemo))
    }

    private fun onFailureDeleteMemo(e: Throwable) {
        logger.e { "메모 삭제 실패: ${e.message}" }
        getMemoList()
    }

    /**---------------------------------------------기타 상태 처리----------------------------------------------*/

    fun toggleMemoShrink() {
        if (uiState.value.memoList.isEmpty()) getMemoList()
        updateState(uiState.value.copy(isMemoShrink = !uiState.value.isMemoShrink))
    }
}

fun <T> MutableList<T>.move(
    from: Int,
    to: Int,
) {
    if (from == to) return
    if (from !in 0 until size || to !in 0..size) return
    val element = this.removeAt(from) ?: return
    val safeToIndex = to.coerceIn(0, size)
    this.add(safeToIndex, element)
}
