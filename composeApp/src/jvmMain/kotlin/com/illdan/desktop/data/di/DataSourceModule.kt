package com.illdan.desktop.data.di

import com.illdan.desktop.data.local.datasource.TodoLocalDataSourceImpl
import com.illdan.desktop.domain.datasource.TodoLocalDataSource
import org.koin.dsl.module

val dataSourceModule = module {
    single<TodoLocalDataSource> { TodoLocalDataSourceImpl(get()) }
}