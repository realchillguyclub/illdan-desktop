package com.illdan.desktop.data.model.response.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthInfoResponse(
    val accessToken: String,
    val refreshToken: String,
    val isNewUser: Boolean,
    val userId: Long
)
