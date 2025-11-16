package com.illdan.desktop.data.repository

import com.illdan.desktop.core.network.NetworkClient
import com.illdan.desktop.core.network.base.BaseRepository
import com.illdan.desktop.data.mapper.AuthUrlResponseMapper
import com.illdan.desktop.domain.enums.HttpMethod
import com.illdan.desktop.domain.model.auth.AuthTokens
import com.illdan.desktop.domain.model.auth.AuthUrl
import com.illdan.desktop.domain.model.request.auth.LoginRequest
import com.illdan.desktop.domain.model.response.auth.AuthInfo
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
                mapper = AuthUrlResponseMapper
            )
        )
    }

    override suspend fun saveToken(request: AuthTokens): Flow<Result<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun clearToken(): Flow<Result<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun getLocalToken(): Flow<AuthTokens> {
        TODO("Not yet implemented")
    }
}