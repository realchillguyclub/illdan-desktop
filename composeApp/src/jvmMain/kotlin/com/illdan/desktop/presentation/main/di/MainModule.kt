package com.illdan.desktop.presentation.main.di

import com.illdan.desktop.core.navigation.NavGraphContributor
import com.illdan.desktop.presentation.main.MainViewModel
import com.illdan.desktop.presentation.main.navigation.MainNavGraph
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainModule = module {
    viewModelOf(::MainViewModel)
    single<NavGraphContributor>(named("main")) { MainNavGraph }
}