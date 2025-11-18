package com.illdan.desktop.data.mapper

import com.illdan.desktop.core.network.base.Mapper
import com.illdan.desktop.data.model.response.auth.AuthUrlResponse
import com.illdan.desktop.domain.model.auth.AuthUrl

object AuthUrlResponseMapper: Mapper<AuthUrlResponse, AuthUrl> {
    override fun responseToModel(response: AuthUrlResponse?): AuthUrl {
        return response?.let {
            AuthUrl(
                authorizeUrl = it.authorizeUrl,
                state = it.state
            )
        } ?: AuthUrl("")
    }
}