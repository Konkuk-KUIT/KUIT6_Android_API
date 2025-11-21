package com.example.kuit6_android_api.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.datastore: DataStore<Preferences> by preferencesDataStore("Token")
val TOKEN_KEY = stringPreferencesKey("token")
val AUTO_LOGIN_KEY = booleanPreferencesKey("auto_login")

class TokenRepositoryImpl(): TokenRepository {
    override suspend fun saveToken(context: Context, token: String) {
        context.datastore.edit{
            it[TOKEN_KEY] = token
        }
    }

    override suspend fun getToken(context: Context): String? {
        val prefs = context.datastore.data.first()
        return prefs[TOKEN_KEY]
    }

    override suspend fun saveAutoLogin(context: Context, isAutoLogin: Boolean) {
        context.datastore.edit {
            it[AUTO_LOGIN_KEY] = isAutoLogin
        }
    }

    override suspend fun getAutoLogin(context: Context): Boolean {
        val prefs = context.datastore.data.first()
        return prefs[AUTO_LOGIN_KEY] ?: false
    }
}