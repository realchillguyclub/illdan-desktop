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

class MemoRepositoryImpl(
    private val networkClient: NetworkClient,
) : BaseRepository(networkClient),
    MemoRepository {
    override suspend fun saveMemo(request: SaveMemoRequest): Result<MemoId> =
        fetchMapped(
            method = HttpMethod.POST,
            path = "/notes",
            body = request,
            mapper = MemoIdResponseMapper,
        )

    override suspend fun getMemoList(): Result<List<Memo>> =
        fetchMapped(
            method = HttpMethod.GET,
            path = "/notes",
            mapper = MemoListResponseMapper,
        )

    override suspend fun deleteMemo(memoId: Long): Result<Unit> =
        fetch(
            method = HttpMethod.DELETE,
            path = "/notes/$memoId",
        )

    override suspend fun updateMemo(
        memoId: Long,
        request: SaveMemoRequest,
    ): Result<Pair<Long, String>> =
        fetchMapped(
            method = HttpMethod.PUT,
            path = "/notes/$memoId",
            body = request,
            mapper = ModifiedMemoResponseMapper,
        )

    override suspend fun getMemoDetail(memoId: Long): Result<Memo> =
        fetch(
            method = HttpMethod.GET,
            path = "/notes/$memoId",
        )
}
