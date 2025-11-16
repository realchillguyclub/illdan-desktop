package com.illdan.desktop.data.model.response.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthUrlResponse(
    val authorizeUrl: String
)