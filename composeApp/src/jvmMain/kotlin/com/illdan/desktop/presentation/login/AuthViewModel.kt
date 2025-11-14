package com.illdan.desktop.presentation.login

import com.illdan.desktop.BuildKonfig
import com.illdan.desktop.core.ui.base.BaseViewModel
import com.illdan.desktop.core.ui.base.UiState
import com.illdan.desktop.core.util.BrowserManager
import com.illdan.desktop.core.util.RandomIdGenerator
import com.illdan.desktop.domain.repository.AuthRepository

class AuthViewModel(
): BaseViewModel<UiState.Default>(UiState.Default) {
    private val kakaoApiKey = BuildKonfig.KAKAO_API_KEY
    private val redirectUri = BuildKonfig.REDIRECT_URI

    fun kakaoLogin() {
        val state = RandomIdGenerator.generateRandomState()
        val kakaoLoginUrl =
            "https://kauth.kakao.com/oauth/authorize?" +
                    "client_id=$kakaoApiKey&" +
                    "redirect_uri=$redirectUri&" +
                    "response_type=code&" +
                    "prompt=login"

        BrowserManager.open(kakaoLoginUrl)
    }
}