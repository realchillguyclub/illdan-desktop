package com.illdan.desktop.domain.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthTokens(
    val accessToken: String = "",
    val refreshToken: String = ""
)
