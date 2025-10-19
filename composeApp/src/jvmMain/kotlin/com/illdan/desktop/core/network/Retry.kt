package com.illdan.desktop.core.network

import kotlinx.coroutines.delay

suspend fun <T> retryResult(
    retries: Int = 0,
    initialDelayMs: Long = 300,
    maxDelayMs: Long = 5_000,
    factor: Double = 2.0,
    block: suspend () -> Result<T>
): Result<T> {
    var attempt = 0
    var delayMs = initialDelayMs
    while (true) {
        val result = block()
        if (result.isSuccess || attempt >= retries) return result
        attempt++
        delay(delayMs)
        delayMs = (delayMs * factor).toLong().coerceAtMost(maxDelayMs)
    }
}