package com.illdan.desktop.presentation.login

import com.illdan.desktop.core.ui.base.BaseViewModel
import com.illdan.desktop.core.ui.base.UiState
import com.illdan.desktop.domain.repository.AuthRepository

class AuthViewModel(
    private val authRepository: AuthRepository
): BaseViewModel<UiState.Default>(UiState.Default) {
    
}