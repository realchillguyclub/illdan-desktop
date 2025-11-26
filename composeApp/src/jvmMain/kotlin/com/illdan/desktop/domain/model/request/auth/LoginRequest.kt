package com.illdan.desktop.domain.model.request.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val socialType: String = "",
    val accessToken: String = ""
)