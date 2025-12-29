package com.illdan.desktop.domain.error

import okio.IOException

sealed class DomainError: Throwable() {
    data object AuthExpired: DomainError()
    data object UnKnownUser: DomainError()
    data class Network(val e: IOException? = null): DomainError()
    data class Unknown(val e: Throwable? = null): DomainError()
}