package com.illdan.desktop.app.di

import com.illdan.desktop.core.network.di.networkModule
import com.illdan.desktop.data.di.repositoryModule
import com.illdan.desktop.presentation.main.di.mainModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(
            listOf(
                networkModule,
                repositoryModule,
                mainModule
            )
        )
    }
}