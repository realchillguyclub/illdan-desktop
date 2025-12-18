package com.illdan.desktop.data.mapper

import com.illdan.desktop.core.network.base.Mapper
import com.illdan.desktop.data.model.response.memo.MemoIdResponse
import com.illdan.desktop.domain.model.memo.MemoId

object MemoIdResponseMapper: Mapper<MemoIdResponse, MemoId> {
    override fun responseToModel(response: MemoIdResponse?): MemoId {
        return response?.let {
            MemoId(
                memoId = it.memoId
            )
        } ?: MemoId()
    }
}