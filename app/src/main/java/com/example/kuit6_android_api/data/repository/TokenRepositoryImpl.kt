package com.example.kuit6_android_api.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlin.text.set

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "Token")
val TOKEN_KEY = stringPreferencesKey("token")
//키 객체를 "token"으로 명명하고 짝이 될 값의 타입을 String으로 정의
val AUTO_LOGIN_KEY = booleanPreferencesKey("auto_login")

class TokenRepositoryImpl:TokenRepository {
    override suspend fun saveToken(context: Context, token: String) {
        context.datastore.edit{
            it[TOKEN_KEY] = token
        }
    }
    override suspend fun getToken(context:Context): String? {
        val prefs = context.datastore.data.first()
        return prefs[TOKEN_KEY]
    }
    
    override suspend fun saveAutoLogin(context: Context, isAutoLogin: Boolean) {
        //자동 로그인 값 저장 함수
        context.datastore.edit {
            it[AUTO_LOGIN_KEY] = isAutoLogin
        }
    }
    
    override suspend fun getAutoLogin(context: Context): Boolean {
        val prefs = context.datastore.data.first()
        //.data를 통해 반환되는 플로우 형태의 데이터
        //해당 플로우에서 방출되는 첫 번째 값, 즉, 현재 저장된 값
        return prefs[AUTO_LOGIN_KEY] ?: false
    }
}