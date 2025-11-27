package com.example.kuit6_android_api.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

val Context.datastore: DataStore<Preferences> by preferencesDataStore("Token")
val KEY = stringPreferencesKey("token")
val AUTO_LOGIN_KEY = booleanPreferencesKey("auto_login")

//컨텍스트를 함수 인자로 넘겨주는 거를 없애줬다.
@Singleton
class TokenRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): TokenRepository {

    override suspend fun saveToken(token: String) {
        context.datastore.edit{
            it[KEY] = token
        }
    }

    override suspend fun getToken(): String? {
        val prefs = context.datastore.data.first()
        return prefs[KEY]
    }

    override suspend fun saveAutoLogin(isAutoLogin: Boolean) {
        context.datastore.edit {
            it[AUTO_LOGIN_KEY] = isAutoLogin
        }
    }

    override suspend fun getAutoLogin(): Boolean {
        val prefs = context.datastore.data.first()
        return prefs[AUTO_LOGIN_KEY] ?: false
    }

    override suspend fun deleteToken() {
        context.datastore.edit {
            it.remove(KEY)
            it.remove(AUTO_LOGIN_KEY)
        }
    }
}