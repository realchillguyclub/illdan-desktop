package com.illdan.desktop.domain.model.request.auth

import kotlinx.serialization.Serializable

@Serializable
data class ReissueRequest(
    val accessToken: String = "",
    val refreshToken: String = "",
    val clientId: String = "",
    val mobileType: String = "DESKTOP"
)
