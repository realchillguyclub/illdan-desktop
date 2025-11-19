package com.illdan.desktop.core.util

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object GlobalEventManager {
    private val _navigateToLoginScreen: MutableSharedFlow<Unit> = MutableSharedFlow()
    val navigateToLoginScreen = _navigateToLoginScreen.asSharedFlow()

    suspend fun navigateToLogin() {
        _navigateToLoginScreen.emit(Unit)
    }
}