package com.illdan.desktop.presentation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illdan.desktop.core.design_system.Gray100
import com.illdan.desktop.core.design_system.Gray90
import com.illdan.desktop.core.design_system.PLACEHOLDER_TEXT_FILED
import com.illdan.desktop.core.design_system.components.CategoryListItem
import com.illdan.desktop.core.design_system.components.RoundedOutlineTextField
import com.illdan.desktop.core.design_system.components.SideBar
import com.illdan.desktop.core.design_system.components.TodoItem
import com.illdan.desktop.domain.enums.TodoStatus
import com.illdan.desktop.domain.model.category.Category
import com.illdan.desktop.domain.model.todo.Todo
import illdandesktop.composeapp.generated.resources.Res
import illdandesktop.composeapp.generated.resources.ic_top_banner
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val interactionSource = remember { MutableInteractionSource() }

    MainContent(
        uiState = uiState,
        interactionSource = interactionSource,
        onEnterClicked = viewModel::createTodo,
        onCategoryClicked = viewModel::updateCurrentCategory,
        onMove = { from, to -> viewModel.onMove(from, to) },
        onShrinkChange = viewModel::toggleSideBarShrink,
        onCheckedChange = viewModel::updateTodoStatus
    )
}

@Composable
private fun MainContent(
    uiState: MainUiState,
    interactionSource: MutableInteractionSource,
    onEnterClicked: (String) -> Unit,
    onCategoryClicked: (Int) -> Unit,
    onMove: (Int, Int) -> Unit,
    onShrinkChange: () -> Unit,
    onCheckedChange: (TodoStatus, Long) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SideBar(
            isShrink = uiState.isSideBarShrink,
            onShrinkChange = onShrinkChange
        )

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
                categoryList = uiState.categoryList,
                itemCounts = listOf(uiState.todayList.size, uiState.todoList.size),
                currentCategory = uiState.currentCategory,
                interactionSource = interactionSource,
                onCategoryClicked = onCategoryClicked
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
                    isToday = uiState.currentCategory.id == -1L,
                    onMove = onMove,
                    onCheckedChange = onCheckedChange
                )
            }
        }
    }
}

@Composable
private fun CategoryList(
    categoryList: List<Category>,
    itemCounts: List<Int>,
    currentCategory: Category,
    interactionSource: MutableInteractionSource,
    onCategoryClicked: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        itemsIndexed(categoryList, key = { _, item -> item.id }) { index, item ->
            CategoryListItem(
                category = item,
                itemCount = itemCounts[index],
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
    onCheckedChange: (TodoStatus, Long) -> Unit
) {
    val seenIds = remember { mutableStateListOf<Long>() }
    val headId = todoList.firstOrNull()?.todoId
    val listState: LazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(listState) { from, to ->
        onMove(from.index, to.index)
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(todoList, key = { it.todoId }) { item ->
            val isNew = remember(item.todoId) { item.todoId !in seenIds }
            val visibleState = remember(item.todoId) {
                MutableTransitionState(!isNew).apply { targetState = true }
            }

            AnimatedVisibility(
                visibleState = visibleState,
                enter = if (isNew)
                    slideInVertically(initialOffsetY = { -it / 2 }) + fadeIn(tween(220))
                else EnterTransition.None,
                exit = ExitTransition.None,
            ) {
                ReorderableItem(
                    reorderableLazyListState,
                    key = item.todoId
                ) { isDragging ->
                    var offsetX by remember { mutableFloatStateOf(0f) }

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
                            onCheckedChange = onCheckedChange
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
            listState.animateScrollToItem(0, 0)
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