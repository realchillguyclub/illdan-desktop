package com.illdan.desktop.presentation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illdan.desktop.core.design_system.Gray100
import com.illdan.desktop.core.design_system.Gray90
import com.illdan.desktop.core.design_system.PLACEHOLDER_TEXT_FILED
import com.illdan.desktop.core.design_system.components.CategoryListItem
import com.illdan.desktop.core.design_system.components.MemoBar
import com.illdan.desktop.core.design_system.components.RoundedOutlineTextField
import com.illdan.desktop.core.design_system.components.SideBar
import com.illdan.desktop.core.design_system.components.TodoItem
import com.illdan.desktop.domain.enums.TodoStatus
import com.illdan.desktop.domain.model.category.Category
import com.illdan.desktop.domain.model.todo.Todo
import illdandesktop.composeapp.generated.resources.Res
import illdandesktop.composeapp.generated.resources.ic_top_banner
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import kotlin.math.roundToInt

@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel(),
    navigateToLoginScreen: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when(event) {
                is MainEvent.NavigateToLogin -> navigateToLoginScreen()
            }
        }
    }

    MainContent(
        uiState = uiState,
        interactionSource = interactionSource,
        onEnterClicked = viewModel::createTodo,
        onCategoryClicked = viewModel::updateCurrentCategory,
        onTodayClicked = viewModel::getTodayList,
        onMove = { from, to -> viewModel.onMove(from, to) },
        onSwiped = viewModel::swipeTodo,
        onShrinkChange = viewModel::toggleSideBarShrink,
        onCheckedChange = viewModel::updateTodoStatus,
        onAllTodoClick = { if (!uiState.isMemoShrink) viewModel.toggleMemoShrink() },
        onMemoClick = viewModel::toggleMemoShrink,
        onMemoSubmit = viewModel::createMemo,
        onBookmarkClick = viewModel::updateTodoBookmark
    )
}

@Composable
private fun MainContent(
    uiState: MainUiState,
    interactionSource: MutableInteractionSource,
    onEnterClicked: (String) -> Unit,
    onCategoryClicked: (Int) -> Unit,
    onTodayClicked: () -> Unit,
    onMove: (Int, Int) -> Unit,
    onSwiped: (Long) -> Unit,
    onShrinkChange: () -> Unit,
    onCheckedChange: (TodoStatus, Long) -> Unit,
    onAllTodoClick: () -> Unit,
    onMemoClick: () -> Unit,
    onMemoSubmit: (Pair<String, String>) -> Unit,
    onBookmarkClick: (Long) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SideBar(
            isShrink = uiState.isSideBarShrink,
            onShrinkChange = onShrinkChange,
            onAllTodoClick = onAllTodoClick,
            onMemoClick = onMemoClick
        )

        AnimatedVisibility(
            visible = !uiState.isMemoShrink,
            modifier = Modifier.background(Gray100),
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally()
        ) {
            MemoBar(
                memoList = uiState.memoList,
                onMemoSubmit = onMemoSubmit
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Gray100),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_top_banner),
                contentDescription = null,
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 228.dp, height = 21.dp)
            )

            Spacer(Modifier.height(20.dp))

            CategoryList(
                today = uiState.todayCategory,
                categoryList = uiState.categoryList,
                currentCategory = uiState.currentCategory,
                interactionSource = interactionSource,
                onCategoryClicked = onCategoryClicked,
                onTodayClicked = onTodayClicked
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Gray90)
                    .padding(horizontal = 20.dp)
                    .padding(top = 16.dp)
            ) {
                TextFieldSection(onDone = onEnterClicked)

                Spacer(Modifier.height(28.dp))

                TodoList(
                    todoList = uiState.currentTodoList,
                    isToday = uiState.currentCategory.id == -2L,
                    onMove = onMove,
                    onSwiped =onSwiped,
                    onCheckedChange = onCheckedChange,
                    onBookmarkClick = onBookmarkClick
                )
            }
        }
    }
}

@Composable
private fun CategoryList(
    today: Category,
    categoryList: List<Category>,
    currentCategory: Category,
    interactionSource: MutableInteractionSource,
    onCategoryClicked: (Int) -> Unit,
    onTodayClicked: () -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        item {
            CategoryListItem(
                category = today,
                isSelected = currentCategory.id == today.id,
                interactionSource = interactionSource,
                onClick = { onTodayClicked() }
            )
        }

        itemsIndexed(categoryList, key = { _, item -> item.id }) { index, item ->
            CategoryListItem(
                category = item,
                isSelected = currentCategory.id == item.id,
                interactionSource = interactionSource,
                onClick = { onCategoryClicked(index) }
            )
        }
    }
}

@Composable
private fun TodoList(
    todoList: List<Todo>,
    isToday: Boolean,
    onMove: (Int, Int) -> Unit,
    onCheckedChange: (TodoStatus, Long) -> Unit,
    onSwiped: (Long) -> Unit,
    onBookmarkClick: (Long) -> Unit
) {
    val seenIds = remember { mutableStateListOf<Long>() }
    val headId = todoList.firstOrNull()?.todoId
    val listState: LazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(listState) { from, to ->
        onMove(from.index, to.index)
    }
    var previousSize by remember { mutableIntStateOf(todoList.size) }
    val scope = rememberCoroutineScope()
    val swipeAnimDuration = 220

    data class ScrollSnapshot(val index: Int, val offset: Int)
    var pendingScrollRestore by remember { mutableStateOf<ScrollSnapshot?>(null) }

    LaunchedEffect(todoList) {
        pendingScrollRestore?.let { snapshot ->
            listState.scrollToItem(snapshot.index, snapshot.offset)
            pendingScrollRestore = null
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(todoList, key = { "${it.todoId}_${it.todoStatus}"  }) { item ->
            val isNew = remember(item.todoId) { item.todoId !in seenIds }
            val visibleState = remember(item.todoId) {
                MutableTransitionState(!isNew).apply { targetState = true }
            }

            AnimatedVisibility(
                visibleState = visibleState,
                enter = if (isNew)
                    slideInVertically(initialOffsetY = { -it / 2 }) + fadeIn(tween(220))
                else EnterTransition.None,
                exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth ->
                        if (isToday) fullWidth else -fullWidth
                    },
                    animationSpec = tween(swipeAnimDuration)
                ) + fadeOut(tween(swipeAnimDuration))
            ) {
                ReorderableItem(
                    reorderableLazyListState,
                    key = item.todoId
                ) { isDragging ->
                    Box(
                        modifier = Modifier
                            .longPressDraggableHandle()
                            .border(
                                if (isDragging) BorderStroke(1.dp, Color.White) else BorderStroke(
                                    0.dp,
                                    Color.Transparent
                                ),
                                RoundedCornerShape(8.dp)
                            )
                            .then(if (isDragging) Modifier.zIndex(1f) else Modifier)
                    ) {
                        TodoItem(
                            item = item,
                            isActive = false,
                            isDeadlineDateMode = false,
                            modifier = Modifier.fillMaxWidth(),
                            isToday = isToday,
                            onCheckedChange = { status, id ->
                                val index = listState.firstVisibleItemIndex
                                val offset = listState.firstVisibleItemScrollOffset
                                pendingScrollRestore = ScrollSnapshot(index, offset)
                                onCheckedChange(status, id)
                            },
                            onSwiped = {
                                scope.launch {
                                    visibleState.targetState = false
                                    delay(swipeAnimDuration.toLong())
                                    onSwiped(it)
                                }
                            },
                            onBookmarkClick = onBookmarkClick
                        )
                    }
                }
            }

            LaunchedEffect(isNew) {
                if (isNew) seenIds += item.todoId
            }
        }
    }

    LaunchedEffect(headId) {
        if (listState.firstVisibleItemIndex != 0 || listState.firstVisibleItemScrollOffset != 0) {
            if (previousSize < todoList.size) {
                previousSize = todoList.size
                listState.animateScrollToItem(0, 0)
            }
        }
    }
}

@Composable
private fun TextFieldSection(
    onDone: (String) -> Unit
) {
    var input by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        RoundedOutlineTextField(
            value = input,
            onValueChange = { input = it },
            placeholder = PLACEHOLDER_TEXT_FILED,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onDone(input); input = "" })
        )
    }
}