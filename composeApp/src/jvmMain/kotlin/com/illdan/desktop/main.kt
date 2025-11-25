package com.illdan.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.svg.SvgDecoder
import com.illdan.desktop.app.di.initKoin
import com.illdan.desktop.presentation.App

@OptIn(ExperimentalCoilApi::class)
fun main() {
    initKoin()

    application {
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
        ) {
            App()
        }
    }
}