package com.illdan.desktop.data.model.response.today

import kotlinx.serialization.Serializable

@Serializable
data class TodayListResponse(
    val date: String = "",
    val todays: List<TodayResponse> = emptyList()
)
