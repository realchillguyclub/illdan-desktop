package com.illdan.desktop.data.di

import com.illdan.desktop.data.repository.TodoRepositoryImpl
import com.illdan.desktop.domain.repository.TodoRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TodoRepository> { TodoRepositoryImpl(get()) }
}