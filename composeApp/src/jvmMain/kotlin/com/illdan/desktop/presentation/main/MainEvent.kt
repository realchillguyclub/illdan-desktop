package com.illdan.desktop.presentation.main

import com.illdan.desktop.core.ui.base.Event

sealed interface MainEvent: Event {
    data object NavigateToLogin: MainEvent
}