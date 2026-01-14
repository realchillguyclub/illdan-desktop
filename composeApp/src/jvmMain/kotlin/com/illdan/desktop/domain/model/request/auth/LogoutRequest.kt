package com.illdan.desktop.domain.model.request.auth

import kotlinx.serialization.Serializable

@Serializable
data class LogoutRequest(
    val mobileType: String = "DESKTOP",
    val clientId: String  = ""
)
