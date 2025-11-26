package com.illdan.desktop.core.network

import co.touchlab.kermit.Logger
import com.illdan.desktop.BuildKonfig
import com.illdan.desktop.core.network.base.ApiException
import com.illdan.desktop.core.network.base.ApiResponse
import com.illdan.desktop.data.local.datastore.AppDataStore
import com.illdan.desktop.domain.enums.HttpMethod
import com.illdan.desktop.domain.error.DomainError
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody

class NetworkClient(
    val dataStore: AppDataStore = AppDataStore
) {
    val logger = Logger.withTag("NetworkClient")
    val httpClient = httpClient()
    val baseUrl: String = BuildKonfig.BASE_URL
        .trim()
        .trim('"')
        .removeSuffix("/")

    fun joinUrl(path: String): String =
        "$baseUrl/${path.trimStart('/')}"

    suspend inline fun <reified T> request(
        method: HttpMethod,
        path: String,
        queryParams: Map<String, Any> = emptyMap(),
        body: Any? = null,
        addAuthHeader: Boolean = true,
        isReissue: Boolean = false
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

                if (body != null) {
                    setBody(body)
                }

                if (addAuthHeader && tokens != null) {
                    val value = if (isReissue) tokens.refreshToken else tokens.accessToken
                    header("Authorization", "Bearer $value")
                    header("X-Mobile-Type", "DESKTOP")
                }
            }

            val apiResponse: ApiResponse<T> = response.body()

            if (apiResponse.isSuccess) {
                val value: T = apiResponse.result ?: (Unit as T)
                Result.success(value)
            } else {
                val domainError = mapApiErrorToDomain(apiResponse.code, apiResponse.message)
                logger.e { "request 실패: $method, $domainError" }
                Result.failure(domainError)
            }
        } catch (e: Exception) {
            logger.e(e) { "request 실패: $method" }
            // Http 타임아웃, IOException과 같은 기타 예외
            Result.failure(mapThrowableToDomain(e))
        }
    }

    fun mapApiErrorToDomain(code: String?, message: String?): DomainError =
        when (code) {
            "AUTH-002" -> DomainError.AuthExpired
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
}