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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl(
    private val networkClient: NetworkClient
): AuthRepository, BaseRepository(networkClient) {
    override suspend fun login(request: LoginRequest): Flow<Result<AuthInfo>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAuthUrl(): Flow<Result<AuthUrl>> = flow {
        emit(
            fetchMapped(
                method = HttpMethod.GET,
                path = "/auth/oauth2/kakao/authorize",
                mapper = AuthUrlResponseMapper,
                addAuthHeader = false
            )
        )
    }

    override suspend fun getAuthInfo(query: String): Flow<Result<AuthInfo>> = flow {
        emit(
            fetchMapped(
                method = HttpMethod.GET,
                path = "/auth/oauth2/kakao/desktop/poll",
                query = mapOf("state" to query),
                mapper = AuthInfoResponseMapper,
                addAuthHeader = false
            )
        )
    }

    override suspend fun logout(): Result<Unit> {
        return fetch(
            method = HttpMethod.POST,
            path = "/auth/logout",
            body = LogoutRequest()
        )
    }

    override suspend fun saveToken(request: AuthTokens) {
        AppDataStore.saveTokens(request)
    }

    override suspend fun clearToken() {
        AppDataStore.clearTokens()
    }

    override suspend fun getLocalToken(): Flow<AuthTokens> = AppDataStore.getTokens()

    override suspend fun getLocalTokenOnce(): AuthTokens? {
        return AppDataStore.getTokensOnce()
    }
}