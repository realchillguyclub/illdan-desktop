package com.illdan.desktop.data.mapper

import com.illdan.desktop.core.network.base.Mapper
import com.illdan.desktop.data.model.response.memo.MemoListResponse
import com.illdan.desktop.domain.model.memo.Memo

object MemoListResponseMapper: Mapper<MemoListResponse, List<Memo>> {
    override fun responseToModel(response: MemoListResponse?): List<Memo> {
        return response?.let {
            it.notes.map { response ->
                Memo(
                    noteId = response.noteId,
                    title = response.title ?: "",
                    content = response.content ?: "",
                    modifyDate = response.modifyDate
                )
            }
        } ?: emptyList()
    }
}