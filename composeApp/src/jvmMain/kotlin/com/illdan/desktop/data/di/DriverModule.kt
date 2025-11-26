package com.illdan.desktop.data.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.illdan.desktop.AppDatabase
import org.koin.dsl.module
import java.nio.file.Paths

val driverModule = module {
    single<SqlDriver> {
        // DB 파일 경로 결정
        val dbPath = Paths.get(
            System.getProperty("user.home"),
            ".illdan",
            "illdan.db"
        ).toFile()

        dbPath.parentFile?.mkdirs()
        val isNew = !dbPath.exists()
        val driver = JdbcSqliteDriver("jdbc:sqlite:${dbPath.absolutePath}")

        driver.execute(null, "PRAGMA foreign_keys = ON", 0)
        driver.execute(null, "PRAGMA journal_mode = WAL", 0)

        if (isNew) {
            AppDatabase.Schema.create(driver)
        }

        driver
    }
}