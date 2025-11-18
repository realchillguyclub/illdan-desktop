package com.illdan.desktop.domain.repository

import com.illdan.desktop.domain.model.auth.AuthInfo
import com.illdan.desktop.domain.model.auth.AuthTokens
import com.illdan.desktop.domain.model.auth.AuthUrl
import com.illdan.desktop.domain.model.request.auth.LoginRequest
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(request: LoginRequest): Flow<Result<AuthInfo>>
    suspend fun getAuthUrl(): Flow<Result<AuthUrl>>
    suspend fun getAuthInfo(query: String): Flow<Result<AuthInfo>>
//    suspend fun reissueToken(request: ReissueRequestModel): Flow<Result<AuthTokens>>
//    suspend fun logout(request: LogoutRequestModel): Flow<Result<Unit>>
    suspend fun saveToken(request: AuthTokens): Flow<Result<Unit>>
    suspend fun clearToken(): Flow<Result<Unit>>
    suspend fun getLocalToken(): Flow<AuthTokens>
}