package com.illdan.desktop.domain.model.auth

data class AuthInfo(
    val authToken: AuthTokens = AuthTokens(),
    val isNewUser: Boolean = false,
    val userId: Long = -1
)
