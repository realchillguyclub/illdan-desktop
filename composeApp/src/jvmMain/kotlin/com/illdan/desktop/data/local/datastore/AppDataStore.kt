package com.illdan.desktop.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import com.illdan.desktop.domain.model.auth.AuthTokens
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
            }
        )

    private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
    private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")

    val accessTokenFlow: Flow<String?> = dataStore.data.map { prefs -> prefs[KEY_ACCESS_TOKEN] }
    val refreshTokenFlow: Flow<String?> = dataStore.data.map { prefs -> prefs[KEY_REFRESH_TOKEN] }

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

    fun getTokens(): Flow<AuthTokens> =
        dataStore.data.map { prefs ->
            AuthTokens(
                accessToken = prefs[KEY_ACCESS_TOKEN].orEmpty(),
                refreshToken = prefs[KEY_REFRESH_TOKEN].orEmpty()
            )
        }
}