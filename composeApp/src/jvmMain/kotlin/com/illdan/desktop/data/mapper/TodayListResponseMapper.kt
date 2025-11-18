package com.illdan.desktop.data.mapper

import com.illdan.desktop.core.network.base.Mapper
import com.illdan.desktop.data.model.response.today.TodayListResponse
import com.illdan.desktop.domain.model.today.TodayListInfo

object TodayListResponseMapper: Mapper<TodayListResponse, TodayListInfo> {
    override fun responseToModel(response: TodayListResponse?): TodayListInfo {
        return response?.let {
            TodayListInfo(
                date = it.date,
                todays = it.todays.map { todayResponse ->  TodayResponseMapper.responseToModel(todayResponse) }
            )
        } ?: TodayListInfo()
    }
}