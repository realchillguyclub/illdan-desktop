package com.illdan.desktop.core.network

import co.touchlab.kermit.Logger
import com.illdan.desktop.BuildKonfig
import com.illdan.desktop.core.network.base.ApiException
import com.illdan.desktop.core.network.base.ApiResponse
import com.illdan.desktop.data.local.datastore.AppDataStore
import com.illdan.desktop.domain.enums.HttpMethod
import com.illdan.desktop.domain.error.DomainError
import com.illdan.desktop.domain.model.auth.AuthTokens
import com.illdan.desktop.domain.model.request.auth.ReissueRequest
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class NetworkClient(
    val dataStore: AppDataStore = AppDataStore
) {
    val logger = Logger.withTag("NetworkClient")
    val httpClient = httpClient()
    val baseUrl: String = BuildKonfig.BASE_URL
        .trim()
        .trim('"')
        .removeSuffix("/")
    val refreshMutex = Mutex()
    val reissuePath = "/auth/refresh"

    fun joinUrl(path: String): String =
        "$baseUrl/${path.trimStart('/')}"

    suspend inline fun <reified T> request(
        method: HttpMethod,
        path: String,
        queryParams: Map<String, Any> = emptyMap(),
        body: Any? = null,
        addAuthHeader: Boolean = true,
        isReissue: Boolean = false,
        retryOnAuth: Boolean = true
    ): Result<T> = requestImpl(
        method = method,
        path = path,
        queryParams = queryParams,
        body = body,
        addAuthHeader = addAuthHeader,
        isReissue = isReissue,
        retryOnAuth = retryOnAuth,
        deserialize = { response -> response.body<ApiResponse<T>>() }   // ★ T 디코딩은 여기서만 reified 사용
    )

    suspend fun <T> requestImpl(
        method: HttpMethod,
        path: String,
        queryParams: Map<String, Any>,
        body: Any?,
        addAuthHeader: Boolean,
        isReissue: Boolean,
        retryOnAuth: Boolean,
        deserialize: suspend (io.ktor.client.statement.HttpResponse) -> ApiResponse<T>
    ): Result<T> {
        return try {
            val tokens = if (addAuthHeader) dataStore.getTokensOnce() else null

            val response = httpClient.request(joinUrl(path)) {
                logger.d { joinUrl(path) }
                this.method = when (method) {
                    HttpMethod.GET -> io.ktor.http.HttpMethod.Get
                    HttpMethod.POST -> io.ktor.http.HttpMethod.Post
                    HttpMethod.PUT -> io.ktor.http.HttpMethod.Put
                    HttpMethod.PATCH -> io.ktor.http.HttpMethod.Patch
                    HttpMethod.DELETE -> io.ktor.http.HttpMethod.Delete
                }

                queryParams.forEach { (key, value) ->
                    url.parameters.append(key, value.toString())
                }

                if (body != null) setBody(body)

                if (addAuthHeader && tokens != null) {
                    val value = if (isReissue) tokens.refreshToken else tokens.accessToken
                    header("Authorization", "Bearer $value")
                    header("X-Mobile-Type", "DESKTOP")
                }
            }

            val isHttpUnauthorized = response.status == HttpStatusCode.Unauthorized

            val apiResponse = runCatching { deserialize(response) }.getOrNull()

            if (apiResponse?.isSuccess == true) {
                val value: T = apiResponse.result ?: (Unit as T)
                return Result.success(value)
            }

            val code = apiResponse?.code
            val message = apiResponse?.message

            // AUTH-008: refresh 만료 → refresh 시도 X
            if (code == CODE_REFRESH_EXPIRED) {
                return Result.failure(DomainError.AuthExpired)
            }

            // AUTH-002: refresh → 원요청 1회 재시도
            val shouldRefresh =
                addAuthHeader && !isReissue && retryOnAuth && (code == CODE_ACCESS_EXPIRED || isHttpUnauthorized)

            if (shouldRefresh) {
                val refreshed = refreshTokensOnce() // refresh 요청
                if (refreshed.isSuccess) {
                    return requestImpl(
                        method = method,
                        path = path,
                        queryParams = queryParams,
                        body = body,
                        addAuthHeader = addAuthHeader,
                        isReissue = false,
                        retryOnAuth = false,
                        deserialize = deserialize
                    )
                } else {
                    return Result.failure(DomainError.AuthExpired)
                }
            }

            val domainError = mapApiErrorToDomain(code, message, isHttpUnauthorized)
            Result.failure(domainError)

        } catch (e: Exception) {
            Result.failure(mapThrowableToDomain(e))
        }
    }

    /**
     * 여러 요청이 동시에 AUTH-002를 맞아도 refresh는 한 번만 수행.
     */
    suspend fun refreshTokensOnce(): Result<AuthTokens> {
        val before = dataStore.getTokensOnce()

        return refreshMutex.withLock {
            val current = dataStore.getTokensOnce()

            if (before != null && current != null && current.accessToken != before.accessToken) {
                return Result.success(current)
            }
            if (current == null) return Result.failure(DomainError.AuthExpired)

            val reissueResult: Result<AuthTokens> = requestImpl(
                method = HttpMethod.POST,
                path = reissuePath,
                queryParams = emptyMap(),
                body = ReissueRequest(
                    accessToken = current.accessToken,
                    refreshToken = current.refreshToken
                ),
                addAuthHeader = true,
                isReissue = true,
                retryOnAuth = false,
                deserialize = { it.body<ApiResponse<AuthTokens>>() }
            )

            reissueResult.onSuccess { newTokens ->
                dataStore.saveTokens(newTokens)
            }
            reissueResult
        }
    }

    fun mapApiErrorToDomain(code: String?, message: String?, isHttpUnauthorized: Boolean): DomainError =
        when {
            code == CODE_UNKNOWN_USER -> DomainError.UnKnownUser
            code == CODE_ACCESS_EXPIRED -> DomainError.AuthExpired
            code == CODE_REFRESH_EXPIRED -> DomainError.AuthExpired
            isHttpUnauthorized -> DomainError.AuthExpired
            else -> DomainError.Unknown(
                e = ApiException(code ?: "UNKNOWN", message ?: "알 수 없는 에러")
            )
        }

    fun mapThrowableToDomain(t: Throwable): DomainError =
        when (t) {
            is java.io.IOException -> DomainError.Network(t)
            is DomainError -> t
            else -> DomainError.Unknown(t)
        }

    companion object {
        const val CODE_ACCESS_EXPIRED = "AUTH-002"
        const val CODE_REFRESH_EXPIRED = "AUTH-008"
        const val CODE_UNKNOWN_USER = "USER-001"
    }
}