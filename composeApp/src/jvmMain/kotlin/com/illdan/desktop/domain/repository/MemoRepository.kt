package com.illdan.desktop.domain.repository

import com.illdan.desktop.domain.model.memo.Memo
import com.illdan.desktop.domain.model.memo.MemoId
import com.illdan.desktop.domain.model.request.memo.SaveMemoRequest

interface MemoRepository {
    suspend fun saveMemo(request: SaveMemoRequest): Result<MemoId>

    suspend fun getMemoList(): Result<List<Memo>>

    suspend fun deleteMemo(memoId: Long): Result<Unit>

    suspend fun updateMemo(
        memoId: Long,
        request: SaveMemoRequest,
    ): Result<Pair<Long, String>>
}
