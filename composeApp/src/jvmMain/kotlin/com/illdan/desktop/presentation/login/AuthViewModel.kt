package com.illdan.desktop.presentation.login

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.illdan.desktop.core.ui.base.BaseViewModel
import com.illdan.desktop.core.ui.base.UiState
import com.illdan.desktop.core.util.BrowserManager
import com.illdan.desktop.domain.model.auth.AuthUrl
import com.illdan.desktop.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
): BaseViewModel<UiState.Default>(UiState.Default) {
    private val logger = Logger.withTag("AuthViewModel")

    fun kakaoLogin() {
        viewModelScope.launch {
            authRepository.getAuthUrl().collect {
                resultResponse(it, ::onSuccessGetAuthUrl)
            }
        }
    }

    private fun onSuccessGetAuthUrl(result: AuthUrl) {
        logger.d { "onSuccessGetAuthUrl: $result" }
        BrowserManager.open(result.authorizeUrl)
    }
}