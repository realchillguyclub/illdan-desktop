package com.illdan.desktop.app.di

import com.illdan.desktop.core.network.di.networkModule
import com.illdan.desktop.data.di.dataSourceModule
import com.illdan.desktop.data.di.databaseModule
import com.illdan.desktop.data.di.driverModule
import com.illdan.desktop.data.di.repositoryModule
import com.illdan.desktop.presentation.main.di.mainModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(
            listOf(
                driverModule,
                databaseModule,
                dataSourceModule,
                networkModule,
                repositoryModule,
                mainModule
            )
        )
    }
}