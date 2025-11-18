package com.illdan.desktop.data.mapper

import com.illdan.desktop.core.network.base.Mapper
import com.illdan.desktop.data.model.response.auth.AuthInfoResponse
import com.illdan.desktop.domain.model.auth.AuthInfo
import com.illdan.desktop.domain.model.auth.AuthTokens

object AuthInfoResponseMapper: Mapper<AuthInfoResponse, AuthInfo> {
    override fun responseToModel(response: AuthInfoResponse?): AuthInfo {
        return response?.let {
            AuthInfo(
                authToken = AuthTokens(accessToken = it.accessToken, refreshToken = it.refreshToken),
                isNewUser = it.isNewUser,
                userId = it.userId
            )
        } ?: AuthInfo()
    }
}