package com.illdan.desktop

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.svg.SvgDecoder
import com.illdan.desktop.app.di.initKoin
import com.illdan.desktop.presentation.App
import java.awt.Dimension

@OptIn(ExperimentalCoilApi::class)
fun main() {
    initKoin()

    application {
        val windowState = rememberWindowState(
            size = DpSize(900.dp, 600.dp)
        )

        setSingletonImageLoaderFactory { context ->
            ImageLoader.Builder(context)
                .components {
                    add(SvgDecoder.Factory())
                }
                .build()
        }

        Window(
            onCloseRequest = ::exitApplication,
            title = "Illdandesktop",
            state = windowState
        ) {
            LaunchedEffect(Unit) {
                window.minimumSize = Dimension(900, 600)
            }

            App()
        }
    }
}