package com.skilllaunch.app.core.session

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.skillLaunchDataStore by preferencesDataStore(
    name = "skilllaunch_session"
)

class SessionStore(
    private val context: Context
) {
    private companion object {
        val ACCESS_TOKEN: Preferences.Key<String> =
            stringPreferencesKey("access_token")
    }

    val accessToken: Flow<String?> =
        context.skillLaunchDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(androidx.datastore.preferences.core.emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[ACCESS_TOKEN]
            }

    suspend fun saveAccessToken(token: String) {
        context.skillLaunchDataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = token
        }
    }

    suspend fun clearSession() {
        context.skillLaunchDataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN)
        }
    }
}