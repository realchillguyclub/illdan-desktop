package com.illdan.desktop.app.di

import com.illdan.desktop.core.network.NetworkClient
import com.illdan.desktop.core.network.TokenProvider
import org.koin.dsl.module

val networkModule = module {
    single<NetworkClient> { NetworkClient(get()) }
    single<TokenProvider> { TokenProvider() }
}