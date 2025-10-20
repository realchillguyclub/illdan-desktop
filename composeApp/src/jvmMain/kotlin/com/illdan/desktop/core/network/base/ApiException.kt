package com.illdan.desktop.core.network.base

data class ApiException(
    val code: String,
    override val message: String
) : Exception(message)