package com.illdan.desktop.data.repository

import com.illdan.desktop.core.network.NetworkClient
import com.illdan.desktop.core.network.base.BaseRepository
import com.illdan.desktop.data.mapper.TodayListResponseMapper
import com.illdan.desktop.domain.enums.HttpMethod
import com.illdan.desktop.domain.model.response.TodayListInfo
import com.illdan.desktop.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TodoRepositoryImpl(
    private val networkClient: NetworkClient
): TodoRepository, BaseRepository(networkClient) {
    override suspend fun getTodayList(): Flow<Result<TodayListInfo>> = flow {
        emit(
            fetchMapped(
                HttpMethod.GET,
                path = "/todays",
                query = mapOf(
                    "page" to "0",
                    "size" to "200"
                ),
                mapper = TodayListResponseMapper
            )
        )
    }
}