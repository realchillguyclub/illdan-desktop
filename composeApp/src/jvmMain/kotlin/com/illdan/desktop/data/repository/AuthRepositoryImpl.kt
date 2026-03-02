package com.illdan.desktop.data.repository

import com.illdan.desktop.core.network.NetworkClient
import com.illdan.desktop.core.network.base.BaseRepository
import com.illdan.desktop.data.local.datastore.AppDataStore
import com.illdan.desktop.data.mapper.AuthInfoResponseMapper
import com.illdan.desktop.data.mapper.AuthUrlResponseMapper
import com.illdan.desktop.domain.enums.HttpMethod
import com.illdan.desktop.domain.model.auth.AuthInfo
import com.illdan.desktop.domain.model.auth.AuthTokens
import com.illdan.desktop.domain.model.auth.AuthUrl
import com.illdan.desktop.domain.model.request.auth.LoginRequest
import com.illdan.desktop.domain.model.request.auth.LogoutRequest
import com.illdan.desktop.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val networkClient: NetworkClient,
) : BaseRepository(networkClient),
    AuthRepository {
    override suspend fun login(request: LoginRequest): Result<AuthInfo> {
        TODO("Not yet implemented")
    }

    override suspend fun getAuthUrl(): Result<AuthUrl> =
        fetchMapped(
            method = HttpMethod.GET,
            path = "/auth/oauth2/kakao/authorize",
            mapper = AuthUrlResponseMapper,
            addAuthHeader = false,
        )

    override suspend fun getAuthInfo(query: String): Result<AuthInfo> =
        fetchMapped(
            method = HttpMethod.GET,
            path = "/auth/oauth2/kakao/desktop/poll",
            query = mapOf("state" to query),
            mapper = AuthInfoResponseMapper,
            addAuthHeader = false,
        )

    override suspend fun logout(): Result<Unit> =
        fetch(
            method = HttpMethod.POST,
            path = "/auth/logout",
            body = LogoutRequest(),
        )

    override suspend fun saveToken(request: AuthTokens) = Result.success(AppDataStore.saveTokens(request))

    override suspend fun clearToken() = Result.success(AppDataStore.clearTokens())

    override suspend fun getLocalToken(): AuthTokens =
        AuthTokens(
            accessToken = AppDataStore.accessTokenFlow ?: "",
            refreshToken = AppDataStore.refreshTokenFlow ?: "",
        )

    override suspend fun getLocalTokenOnce(): AuthTokens? = AppDataStore.getTokensOnce()
}
