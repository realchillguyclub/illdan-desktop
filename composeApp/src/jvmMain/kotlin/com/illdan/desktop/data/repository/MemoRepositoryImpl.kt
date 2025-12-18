package com.illdan.desktop.data.repository

import com.illdan.desktop.core.network.NetworkClient
import com.illdan.desktop.core.network.base.BaseRepository
import com.illdan.desktop.domain.enums.HttpMethod
import com.illdan.desktop.domain.model.request.memo.SaveMemoRequest
import com.illdan.desktop.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MemoRepositoryImpl(
    private val networkClient: NetworkClient
): MemoRepository, BaseRepository(networkClient) {
    override suspend fun saveMemo(request: SaveMemoRequest): Flow<Result<Unit>> = flow {
        emit(
            fetch(
                method = HttpMethod.POST,
                path = "/notes",
                body = request
            )
        )
    }
}