package com.illdan.desktop.domain.model.today

import com.illdan.desktop.domain.model.todo.Todo

data class TodayListInfo(
    val date: String = "",
    val todays: List<Todo> = emptyList()
)