package com.illdan.desktop.presentation.login.di

import com.illdan.desktop.core.navigation.NavGraphContributor
import com.illdan.desktop.presentation.login.AuthViewModel
import com.illdan.desktop.presentation.login.navigation.AuthNavGraph
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val loginModule = module {
    single<NavGraphContributor> { AuthNavGraph }
    viewModelOf(::AuthViewModel)
}