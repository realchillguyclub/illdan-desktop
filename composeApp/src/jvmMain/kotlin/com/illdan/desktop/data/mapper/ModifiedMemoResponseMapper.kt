package com.illdan.desktop.data.mapper

import com.illdan.desktop.core.network.base.Mapper
import com.illdan.desktop.data.model.response.memo.ModifiedMemoResponse

object ModifiedMemoResponseMapper: Mapper<ModifiedMemoResponse, Pair<Long, String>> {
    override fun responseToModel(response: ModifiedMemoResponse?): Pair<Long, String> {
        return response?.let {
            it.noteId to it.modifyDate
        } ?: (-1L to "")
    }
}