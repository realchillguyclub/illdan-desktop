package com.illdan.desktop.domain.repository

import com.illdan.desktop.domain.model.memo.Memo
import com.illdan.desktop.domain.model.memo.MemoId
import com.illdan.desktop.domain.model.request.memo.SaveMemoRequest
import kotlinx.coroutines.flow.Flow

interface MemoRepository {
    suspend fun saveMemo(request: SaveMemoRequest): Flow<Result<MemoId>>
    suspend fun getMemoList(): Flow<Result<List<Memo>>>
    suspend fun deleteMemo(memoId: Long): Flow<Result<Unit>>
    suspend fun updateMemo(memoId: Long, request: SaveMemoRequest): Flow<Result<MemoId>>
}