package com.illdan.desktop.core.network

import co.touchlab.kermit.Logger
import com.illdan.desktop.BuildKonfig
import com.illdan.desktop.core.network.base.ApiException
import com.illdan.desktop.core.network.base.ApiResponse
import com.illdan.desktop.data.local.datastore.AppDataStore
import com.illdan.desktop.domain.enums.HttpMethod
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.first

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

            if (apiResponse.isSuccess && apiResponse.result != null) {
                Result.success(apiResponse.result)
            } else {
                Result.failure(ApiException(apiResponse.code, apiResponse.message))
            }
        } catch (e: Exception) {
            logger.e(e) { "request 실패: $method" }
            Result.failure(e)
        }
    }
}