package com.illdan.desktop.presentation.main

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.illdan.desktop.core.ui.base.BaseViewModel
import com.illdan.desktop.domain.enums.TodoStatus
import com.illdan.desktop.domain.enums.TodoType
import com.illdan.desktop.domain.model.category.Category
import com.illdan.desktop.domain.model.category.GroupEmoji
import com.illdan.desktop.domain.model.memo.Memo
import com.illdan.desktop.domain.model.memo.MemoId
import com.illdan.desktop.domain.model.request.category.CreateCategoryRequest
import com.illdan.desktop.domain.model.request.memo.SaveMemoRequest
import com.illdan.desktop.domain.model.request.todo.CreateTodoRequest
import com.illdan.desktop.domain.model.request.todo.GetTodoListRequest
import com.illdan.desktop.domain.model.request.todo.ReorderTodoListRequest
import com.illdan.desktop.domain.model.request.todo.TodoId
import com.illdan.desktop.domain.model.today.TodayListInfo
import com.illdan.desktop.domain.model.todo.Todo
import com.illdan.desktop.domain.repository.AuthRepository
import com.illdan.desktop.domain.repository.CategoryRepository
import com.illdan.desktop.domain.repository.MemoRepository
import com.illdan.desktop.domain.repository.TodoRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(
    private val authRepository: AuthRepository,
    private val todoRepository: TodoRepository,
    private val categoryRepository: CategoryRepository,
    private val memoRepository: MemoRepository
): BaseViewModel<MainUiState>(MainUiState()) {
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

    // 카테고리 이모지 조회
    fun getEmojis() {
        if (uiState.value.emojiList.groupEmojis.isNotEmpty()) return

        viewModelScope.launch {
            categoryRepository.getEmojiList().collect {
                resultResponse(it, ::onSuccessGetEmojis)
            }
        }
    }

    private fun onSuccessGetEmojis(result: GroupEmoji) {
        updateState(uiState.value.copy(emojiList = result))
    }

    // 카테고리 생성
    fun createCategory(name: String, emojiId: Long) {
        viewModelScope.launch {
            categoryRepository.createCategory(request = CreateCategoryRequest(name, emojiId)).collect {
                resultResponse(it, ::onSuccessCreateCategory)
            }
        }
    }

    private fun onSuccessCreateCategory(result: Unit) {
        getCategoryList()
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

    /**---------------------------------------------할 일 스와이프----------------------------------------------*/
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
            getTodayList()
        } else {
            getTodoList(category)
        }
    }

    /**---------------------------------------------할 일 체크----------------------------------------------*/
    fun updateTodoStatus(currentStatus: TodoStatus, id: Long) {
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
                currentTodoList = if (uiState.value.currentCategory.id == -2L) newList else uiState.value.currentTodoList
            )
        )

        viewModelScope.launch {
            todoRepository.updateTodoStatus(todoId = id).collect {
                resultResponse(it, {}, ::onFailUpdateTodoStatus)
            }
        }
    }

    private fun onFailUpdateTodoStatus(e: Throwable) {
        getTodayList()
    }

    /**---------------------------------------------할 일 북마크----------------------------------------------*/

    fun updateTodoBookmark(todoId: Long) {
        updateTodoBookmarkInUI(todoId)

        viewModelScope.launch {
            todoRepository.updateTodoBookmark(todoId = todoId).collect {
                resultResponse(it, {}, ::onFailUpdateTodoBookmark)
            }
        }
    }

    private fun updateTodoBookmarkInUI(todoId: Long) {
        val todo = uiState.value.currentTodoList.firstOrNull { it.todoId == todoId }

        if (todo == null) return

        val newTodo = todo.copy(isBookmark = !todo.isBookmark)
        val newList = uiState.value.currentTodoList.map { if (it.todoId == todoId) newTodo else it }

        updateState(uiState.value.copy(currentTodoList = newList))
    }

    private fun onFailUpdateTodoBookmark(e: Throwable) {
        getTodoList(category = uiState.value.currentCategory)
    }

    /**---------------------------------------------할 일 드래그 앤 드롭----------------------------------------------*/

    fun onMove(from: Int, to: Int) {
        val currentList = uiState.value.currentTodoList.toMutableList()

        if (currentList[from].todoStatus == TodoStatus.COMPLETED || currentList[to].todoStatus == TodoStatus.COMPLETED) return

        currentList.move(from, to)
        onDragEnd(currentList)
        updateState(uiState.value.copy(currentTodoList = currentList))
    }

    fun onDragEnd(newList: List<Todo>) {
        val todoIdList = newList.map { it.todoId }
        val todoType = if (uiState.value.currentCategory.id == -2L) TodoType.TODAY else TodoType.BACKLOG

        viewModelScope.launch {
            todoRepository.reorderTodoList(request = ReorderTodoListRequest(type = todoType.name, todoIds = todoIdList)).collect {
                resultResponse(it, {}, { onFailReorderTodoList(todoType) })
            }
        }
    }

    private fun onFailReorderTodoList(todoType: TodoType) {
        when(todoType) {
            TodoType.TODAY -> getTodayList()
            TodoType.BACKLOG -> getTodoList(uiState.value.currentCategory)
        }
    }

    /**---------------------------------------------메모----------------------------------------------*/

    // 메모 생성
    fun createMemo() {
        viewModelScope.launch {
            memoRepository.saveMemo(request = SaveMemoRequest("", "")).collect {
                resultResponse(it, ::onSuccessCreateMemo)
            }
        }
    }

    private fun onSuccessCreateMemo(result: MemoId) {
        val curList = uiState.value.memoList.toMutableList()
        val newMemo = Memo(noteId = result.memoId, title = "", content = "")
        curList.add(0, newMemo)

        updateStateSync(uiState.value.copy(memoList = curList, selectedMemo = newMemo))
    }

    // 메모 저장
    fun saveMemo(id: Long, input: Pair<String, String>) {
        val memo = uiState.value.memoList.firstOrNull { it.noteId == id }

        if (memo == null) {
            logger.d { "수정할 메모를 찾지 못했습니다. id: $id" }
            return
        }

        val updatedMemo = memo.copy(title = input.first, content = input.second)

        updateMemoInUi(updatedMemo)

        // 서버 저장은 debounce
        saveJobs[id]?.cancel()
        saveJobs[id] = viewModelScope.launch {
            delay(400) // debounce window
            memoRepository
                .updateMemo(updatedMemo.noteId, SaveMemoRequest(updatedMemo.title, updatedMemo.content))
                .collect { result ->
                    resultResponse(result, {})
                }
        }
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
        } else updateState(uiState.value.copy(selectedMemo = memo))
    }

    // 메모 리스트 조회
    fun getMemoList() {
        viewModelScope.launch {
            memoRepository.getMemoList().collect {
                resultResponse(it, ::onSuccessGetMemoList)
            }
        }
    }

    private fun onSuccessGetMemoList(result: List<Memo>) {
        updateState(uiState.value.copy(memoList = result))
    }

    // 메모 삭제
    fun deleteMemo(memoId: Long) {
        deleteMemoInUI(memoId)

        viewModelScope.launch {
            memoRepository.deleteMemo(memoId).collect {
                resultResponse(it, {}, ::onFailDeleteMemo)
            }
        }
    }

    private fun deleteMemoInUI(memoId: Long) {
        val curList = uiState.value.memoList.toMutableList()
        val index = curList.indexOfFirst { it.noteId == memoId }

        if (index == -1) return

        curList.removeAt(index)

        updateStateSync(uiState.value.copy(memoList = curList, selectedMemo = emptyMemo))
    }

    private fun onFailDeleteMemo(e: Throwable) {
        logger.e { "메모 삭제 실패: ${e.message}" }
        getMemoList()
    }

    /**---------------------------------------------기타 상태 처리----------------------------------------------*/
    fun toggleSideBarShrink() {
        updateState(uiState.value.copy(isSideBarShrink = !uiState.value.isSideBarShrink))
    }

    fun toggleMemoShrink() {
        if (uiState.value.memoList.isEmpty()) getMemoList()
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