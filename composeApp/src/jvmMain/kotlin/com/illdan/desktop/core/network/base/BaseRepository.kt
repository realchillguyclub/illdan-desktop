package com.illdan.desktop.core.network.base

import com.illdan.desktop.core.network.NetworkClient
import com.illdan.desktop.core.network.retryResult
import com.illdan.desktop.domain.enums.HttpMethod

abstract class BaseRepository(
    protected val network: NetworkClient
) {
    protected suspend inline fun <reified T> fetch(
        method: HttpMethod,
        path: String,
        body: Any? = null,
        query: Map<String, String> = emptyMap(),
        addAuthHeader: Boolean = true,
        retries: Int = 0,
        isReissue: Boolean = false
    ): Result<T> {
        val client = network

        return retryResult(retries = retries) {
            try {
                client.request<T>(
                    path = path,
                    method = method,
                    body = body,
                    queryParams = query,
                    addAuthHeader = addAuthHeader,
                    isReissue = isReissue
                )
            } catch (t: Throwable) {
                Result.failure(t)
            }
        }
    }

    protected suspend inline fun <reified R, M> fetchMapped(
        method: HttpMethod,
        path: String,
        body: Any? = null,
        query: Map<String, String> = emptyMap(),
        addAuthHeader: Boolean = true,
        retries: Int = 0,
        isReissue: Boolean = false,
        mapper: Mapper<R, M>
    ): Result<M> {
        val raw: Result<R> = fetch(
            method = method,
            path = path,
            body = body,
            query = query,
            addAuthHeader = addAuthHeader,
            retries = retries,
            isReissue = isReissue
        )

        return raw.mapCatching { mapper.responseToModel(it) }
    }
}