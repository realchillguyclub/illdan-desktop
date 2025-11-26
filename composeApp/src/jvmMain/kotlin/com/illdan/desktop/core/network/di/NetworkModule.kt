package com.illdan.desktop.core.network.di

import com.illdan.desktop.core.network.NetworkClient
import org.koin.dsl.module

val networkModule = module {
    single<NetworkClient> { NetworkClient() }
}