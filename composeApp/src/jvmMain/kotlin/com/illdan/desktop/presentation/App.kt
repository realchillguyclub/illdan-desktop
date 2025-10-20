package com.illdan.desktop.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.illdan.desktop.core.design_system.AppTheme
import com.illdan.desktop.core.ui.util.DismissKeyboardOnClick
import com.illdan.desktop.presentation.main.MainScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import illdandesktop.composeapp.generated.resources.Res
import illdandesktop.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    AppTheme {
        DismissKeyboardOnClick {
            MainScreen()
        }
    }
}