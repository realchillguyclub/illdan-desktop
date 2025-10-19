package com.illdan.desktop.core.design_system

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalAppTypography = staticCompositionLocalOf<AppTypography> {
    error("AppTypography가 제공되지 않음")
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val typography = AppTypo()

    CompositionLocalProvider(
        LocalAppTypography provides typography
    ) {
        content()
    }
}