package com.illdan.desktop.app.di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(
            listOf(
                networkModule
            )
        )
    }
}