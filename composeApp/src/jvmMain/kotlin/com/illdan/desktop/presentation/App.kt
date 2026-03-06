package com.illdan.desktop.presentation

import androidx.compose.runtime.*
import com.illdan.desktop.core.designsystem.AppTheme
import com.illdan.desktop.core.navigation.AppNavHost
import com.illdan.desktop.core.ui.util.DismissKeyboardOnClick
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        DismissKeyboardOnClick {
            AppNavHost()
        }
    }
}
