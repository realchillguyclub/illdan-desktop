package com.illdan.desktop.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.illdan.desktop.domain.model.auth.AuthTokens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.io.File

object AppDataStore {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val dataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = scope,
            produceFile = {
                val dir = File(System.getProperty("user.home"), ".illdan_desktop")
                if (!dir.exists()) dir.mkdirs()
                File(dir, "settings.preferences_pb")
            },
        )
    private val tokenState =
        dataStore
            .data
            .map { prefs ->
                val access = prefs[KEY_ACCESS_TOKEN]
                val refresh = prefs[KEY_REFRESH_TOKEN]
                if (access.isNullOrBlank() || refresh.isNullOrBlank()) {
                    null
                } else {
                    AuthTokens(access, refresh)
                }
            }.stateIn(
                scope = scope,
                started = SharingStarted.Eagerly,
                initialValue = null,
            )

    private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
    private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")

    val accessTokenFlow: String?
        get() = tokenState.value?.accessToken
    val refreshTokenFlow: String?
        get() = tokenState.value?.refreshToken

    suspend fun saveTokens(token: AuthTokens) {
        dataStore.edit { prefs ->
            prefs[KEY_ACCESS_TOKEN] = token.accessToken
            prefs[KEY_REFRESH_TOKEN] = token.refreshToken
        }
    }

    suspend fun clearTokens() {
        dataStore.edit { prefs ->
            prefs.remove(KEY_ACCESS_TOKEN)
            prefs.remove(KEY_REFRESH_TOKEN)
        }
    }

    suspend fun getTokensOnce(): AuthTokens? =
        dataStore.data.first().let { prefs ->
            val access = prefs[KEY_ACCESS_TOKEN]
            val refresh = prefs[KEY_REFRESH_TOKEN]
            if (access.isNullOrBlank() || refresh.isNullOrBlank()) {
                null
            } else {
                AuthTokens(access, refresh)
            }
        }
}
