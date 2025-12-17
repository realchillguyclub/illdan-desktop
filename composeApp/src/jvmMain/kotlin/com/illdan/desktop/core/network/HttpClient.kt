package com.illdan.desktop.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.accept
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.ConnectionPool
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalSerializationApi::class)
fun httpClient(): HttpClient = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                isLenient = true
                explicitNulls = false
                encodeDefaults = true
            }
        )
    }

    val prettyJson = Json {
        prettyPrint        = true
        prettyPrintIndent  = "  "
        ignoreUnknownKeys  = true
        isLenient          = true
        encodeDefaults     = true
    }

    install(DefaultRequest) {
        headers {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
        }
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 30_000
        connectTimeoutMillis = 15_000
        socketTimeoutMillis = 30_000
    }

    install(ResponseObserver) {
        onResponse { response ->
            if (response.status.value in 200..299 &&
                response.headers[HttpHeaders.ContentType]?.startsWith("application/json") == true) {
                val text = response.bodyAsText()

                try {
                    val element = prettyJson.parseToJsonElement(text)
                    println("Ktor ▶\n${prettyJson.encodeToString(element)}")
                } catch (_: Exception) {
                    println("Ktor ▶\n$text")
                }
            }
        }
    }

    install(Logging) {
        logger = Logger.SIMPLE
        level = LogLevel.BODY
    }

    engine {
        config {
            retryOnConnectionFailure(true)
            connectionPool(ConnectionPool(5, 5, TimeUnit.MINUTES))
        }
    }

    expectSuccess = false
}