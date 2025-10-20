package com.illdan.desktop.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illdan.desktop.core.design_system.Gray100
import com.illdan.desktop.core.design_system.Gray90
import com.illdan.desktop.core.design_system.PLACEHOLDER_TEXT_FILED
import com.illdan.desktop.core.design_system.components.CategoryListItem
import com.illdan.desktop.core.design_system.components.RoundedOutlineTextField
import com.illdan.desktop.core.design_system.components.TodoItem
import com.illdan.desktop.domain.model.category.Category
import com.illdan.desktop.domain.model.todo.Todo
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val interactionSource = remember { MutableInteractionSource() }

    MainContent(
        uiState = uiState,
        interactionSource = interactionSource,
        onEnterClicked = {}
    )
}

@Composable
private fun MainContent(
    uiState: MainUiState,
    interactionSource: MutableInteractionSource,
    onEnterClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray100)
    ) {
        CategoryList(
            categoryList = uiState.categoryList,
            itemCount = uiState.todoList.size,
            currentCategory = uiState.currentCategory,
            interactionSource = interactionSource,
            onCategoryClicked = {}
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
                todoList = uiState.todoList
            )
        }
    }
}

@Composable
private fun CategoryList(
    categoryList: List<Category>,
    itemCount: Int,
    currentCategory: Category,
    interactionSource: MutableInteractionSource,
    onCategoryClicked: (Long) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(categoryList, key = { it.id }) { item ->
            CategoryListItem(
                icon = item.imageUrl,
                title = item.name,
                itemCount = itemCount,
                isSelected = currentCategory.id == item.id,
                interactionSource = interactionSource,
                onClick = { onCategoryClicked(item.id) }
            )
        }
    }
}

@Composable
private fun TodoList(
    todoList: List<Todo>,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(todoList, key = { it.todoId }) { item ->
            TodoItem(
                item = item,
                isActive = false,
                isDeadlineDateMode = false,
                modifier = Modifier.fillMaxWidth(),
                isToday = true,
            )
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
            keyboardActions = KeyboardActions(onDone = { onDone(input) })
        )
    }
}