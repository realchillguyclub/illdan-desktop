package com.illdan.desktop.data.repository

import com.illdan.desktop.core.network.NetworkClient
import com.illdan.desktop.core.network.base.BaseRepository
import com.illdan.desktop.data.mapper.MemoIdResponseMapper
import com.illdan.desktop.data.mapper.MemoListResponseMapper
import com.illdan.desktop.data.mapper.ModifiedMemoResponseMapper
import com.illdan.desktop.domain.enums.HttpMethod
import com.illdan.desktop.domain.model.memo.Memo
import com.illdan.desktop.domain.model.memo.MemoId
import com.illdan.desktop.domain.model.request.memo.SaveMemoRequest
import com.illdan.desktop.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MemoRepositoryImpl(
    private val networkClient: NetworkClient
): MemoRepository, BaseRepository(networkClient) {
    override suspend fun saveMemo(request: SaveMemoRequest): Flow<Result<MemoId>> = flow {
        emit(
            fetchMapped(
                method = HttpMethod.POST,
                path = "/notes",
                body = request,
                mapper = MemoIdResponseMapper
            )
        )
    }

    override suspend fun getMemoList(): Flow<Result<List<Memo>>> = flow {
        emit(
            fetchMapped(
                method = HttpMethod.GET,
                path = "/notes",
                mapper = MemoListResponseMapper
            )
        )
    }

    override suspend fun deleteMemo(memoId: Long): Flow<Result<Unit>> = flow {
        emit(
            fetch(
                method = HttpMethod.DELETE,
                path = "/notes/$memoId"
            )
        )
    }

    override suspend fun updateMemo(
        memoId: Long,
        request: SaveMemoRequest
    ): Flow<Result<Pair<Long, String>>> = flow {
        emit(
            fetchMapped(
                method = HttpMethod.PUT,
                path = "/notes/$memoId",
                body = request,
                mapper = ModifiedMemoResponseMapper
            )
        )
    }
}