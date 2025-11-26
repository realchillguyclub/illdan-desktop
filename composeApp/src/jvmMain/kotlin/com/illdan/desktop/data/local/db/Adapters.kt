package com.illdan.desktop.data.local.db

import app.cash.sqldelight.ColumnAdapter
import com.illdan.desktop.domain.enums.TodoStatus
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

private val json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

// List<String> <-> String
object RoutineDaysAdapter : ColumnAdapter<List<String>, String> {
    private val serializer = ListSerializer(String.serializer())

    override fun decode(databaseValue: String): List<String> =
        if (databaseValue.isBlank()) emptyList()
        else json.decodeFromString(serializer, databaseValue)

    override fun encode(value: List<String>): String =
        json.encodeToString(serializer, value)
}

object TodoStatusAdapter : ColumnAdapter<TodoStatus, String> {
    override fun decode(databaseValue: String) = TodoStatus.valueOf(databaseValue)
    override fun encode(value: TodoStatus) = value.name
}