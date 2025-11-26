package com.illdan.desktop.core.util

import co.touchlab.kermit.Logger

object BrowserManager {
    val logger = Logger.withTag("BrowserManager")

    fun open(url: String) {
        val os = System.getProperty("os.name").lowercase()

        try {
            when {
                os.contains("mac") -> Runtime.getRuntime().exec(arrayOf("open", url))
                os.contains("win") -> Runtime.getRuntime().exec(arrayOf("rundll32", "url.dll,FileProtocolHandler", url))
                else -> Runtime.getRuntime().exec(arrayOf("xdg-open", url))
            }
        } catch (e: Exception) {
            logger.e { "브라우저 열기 실패: ${e.stackTraceToString()}" }
        }
    }
}