package com.illdan.desktop.presentation.login

import com.illdan.desktop.core.ui.base.Event

sealed interface AuthEvent: Event {
    object NavigateToMain: AuthEvent
    object NavigateToLogin: AuthEvent
}