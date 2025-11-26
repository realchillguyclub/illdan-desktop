package com.illdan.desktop.core.util

import java.util.UUID

object RandomIdGenerator {
    fun generateRandomState(): String = UUID.randomUUID().toString()
}