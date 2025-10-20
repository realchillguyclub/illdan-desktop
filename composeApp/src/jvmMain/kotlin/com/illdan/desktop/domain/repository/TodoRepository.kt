package com.illdan.desktop.domain.repository

import com.illdan.desktop.domain.model.response.TodayListInfo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    suspend fun getTodayList(): Flow<Result<TodayListInfo>>
}