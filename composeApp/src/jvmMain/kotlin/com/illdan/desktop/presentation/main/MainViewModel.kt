package com.illdan.desktop.presentation.main

import com.illdan.desktop.core.ui.base.BaseViewModel
import com.illdan.desktop.domain.repository.TodoRepository

class MainViewModel(
    private val todoRepository: TodoRepository
): BaseViewModel<MainUiState>(MainUiState()) {

    init {
        initCategory()
    }

    private fun initCategory() {
        updateState(uiState.value.copy(currentCategory = uiState.value.categoryList.first()))
    }
}