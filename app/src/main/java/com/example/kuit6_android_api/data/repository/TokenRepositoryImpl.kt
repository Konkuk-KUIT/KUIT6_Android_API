package com.example.kuit6_android_api.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.kuit6_android_api.data.model.response.BaseResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

val Context.datastore: DataStore<Preferences> by preferencesDataStore("Token")
val KEY = stringPreferencesKey("token")
val AUTO_LOGIN_KEY = booleanPreferencesKey("auto_login")

@Singleton
class TokenRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): TokenRepository {
    override suspend fun saveToken(token: String) {
        context.datastore.edit {
            it[KEY] = token
        }
    }

    override suspend fun getToken(): String? {
        val prefs = context.datastore.data.first()
        return prefs[KEY]
    }
    override suspend fun saveAutoLogin(enabled: Boolean) {
        context.datastore.edit { prefs ->
            prefs[AUTO_LOGIN_KEY] = enabled
        }
    }
    override suspend fun getAutoLogin(): Boolean {
        val prefs = context.datastore.data.first()
        return prefs[AUTO_LOGIN_KEY] ?: false
    }

    override suspend fun deleteToken() {
        context.datastore.edit { preferences ->
            preferences.remove(KEY)
        }
    }
}