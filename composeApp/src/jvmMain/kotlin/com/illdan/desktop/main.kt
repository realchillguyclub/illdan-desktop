package com.illdan.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.illdan.desktop.app.di.initKoin
import com.illdan.desktop.presentation.App

fun main() {
    initKoin()

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Illdandesktop",
        ) {
            App()
        }
    }
}