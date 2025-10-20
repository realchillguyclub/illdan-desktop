package com.illdan.desktop.domain.model.response.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthInfo(
    val accessToken: String = "",
    val refreshToken: String = "",
    val isNewUser: Boolean = false,
    val userId: Long = -1
)
