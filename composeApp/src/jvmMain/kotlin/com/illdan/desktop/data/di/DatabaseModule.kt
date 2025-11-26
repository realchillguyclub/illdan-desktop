package com.illdan.desktop.data.di

import com.illdan.desktop.AppDatabase
import com.illdan.desktop.data.local.db.RoutineDaysAdapter
import com.illdan.desktop.data.local.db.TodoStatusAdapter
import com.illdan.desktop.local.db.TodoEntity
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        AppDatabase(
            get(),
            TodoEntity.Adapter(
                todoStatusAdapter = TodoStatusAdapter,
                routineDaysAdapter = RoutineDaysAdapter
            )
        )
    }
}