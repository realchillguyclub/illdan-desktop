package com.illdan.desktop.domain.repository

import com.illdan.desktop.domain.model.auth.AuthInfo
import com.illdan.desktop.domain.model.auth.AuthTokens
import com.illdan.desktop.domain.model.auth.AuthUrl
import com.illdan.desktop.domain.model.request.auth.LoginRequest

interface AuthRepository {
    suspend fun login(request: LoginRequest): Result<AuthInfo>

    suspend fun getAuthUrl(): Result<AuthUrl>

    suspend fun getAuthInfo(query: String): Result<AuthInfo>

//    suspend fun reissueToken(request: ReissueRequestModel): Flow<Result<AuthTokens>>
    suspend fun logout(): Result<Unit>

    suspend fun saveToken(request: AuthTokens): Result<Unit>

    suspend fun clearToken(): Result<Unit>

    suspend fun getLocalToken(): AuthTokens

    suspend fun getLocalTokenOnce(): AuthTokens?
}
