package com.example.kuit6_android_api.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("Token")
val KEY = stringPreferencesKey("token")
val AUTO_LOGIN_KEY = booleanPreferencesKey("auto_login")

class TokenRepositoryImpl : TokenRepository {
    override suspend fun saveToken(context: Context, token: String) {
        context.dataStore.edit {
            it[KEY] = token
        }
    }

    override suspend fun getToken(context: Context): String?
    {
        val prefs = context.dataStore.data.first()
        return prefs[KEY]
    }

    override suspend fun saveAutoLogin(context: Context, isAutoLogin: Boolean) {
        context.dataStore.edit {
            it[AUTO_LOGIN_KEY] = isAutoLogin
        }
    }

    override suspend fun getAutoLogin(context: Context): Boolean {
        val prefs = context.dataStore.data.first()
        return prefs[AUTO_LOGIN_KEY] ?: false
    }
}