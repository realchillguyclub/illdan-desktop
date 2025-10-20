package com.illdan.desktop.core.network.base

interface Mapper<RESPONSE, MODEL> {
    fun responseToModel(response: RESPONSE?): MODEL
}