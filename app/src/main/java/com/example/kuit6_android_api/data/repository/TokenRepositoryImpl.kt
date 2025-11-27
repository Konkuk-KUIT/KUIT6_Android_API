package com.example.kuit6_android_api.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("Token")
val KEY = stringPreferencesKey("token")
val AUTO_LOGIN_KEY = booleanPreferencesKey("auto_login")

@Singleton
class TokenRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenRepository {

    override suspend fun saveToken(token: String) {
        context.dataStore.edit {
            it[KEY] = token
        }
    }

    override suspend fun getToken(): String?
    {
        val prefs = context.dataStore.data.first()
        return prefs[KEY]
    }

    override suspend fun saveAutoLogin(isAutoLogin: Boolean) {
        context.dataStore.edit {
            it[AUTO_LOGIN_KEY] = isAutoLogin
        }
    }

    override suspend fun getAutoLogin(): Boolean {
        val prefs = context.dataStore.data.first()
        return prefs[AUTO_LOGIN_KEY] ?: false
    }
}