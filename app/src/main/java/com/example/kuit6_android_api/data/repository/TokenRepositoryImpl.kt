package com.example.kuit6_android_api.data.repository

import android.content.Context
import android.util.Log
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
import kotlin.text.set

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "Token")
val TOKEN_KEY = stringPreferencesKey("token")
//키 객체를 "token"으로 명명하고 짝이 될 값의 타입을 String으로 정의
val AUTO_LOGIN_KEY = booleanPreferencesKey("auto_login")

@Singleton
class TokenRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context //Hilt가 Context 자동 주입
):TokenRepository {

    override suspend fun saveToken(token: String) {
        context.datastore.edit{
            it[TOKEN_KEY] = token
        }
    }
    override suspend fun getToken(): String? {
        val prefs = context.datastore.data.first()
        return prefs[TOKEN_KEY]
    }
    
    override suspend fun saveAutoLogin(isAutoLogin: Boolean) {
        //자동 로그인 값 저장 함수
        context.datastore.edit {
            it[AUTO_LOGIN_KEY] = isAutoLogin
        }
    }
    
    override suspend fun getAutoLogin(): Boolean {
        val prefs = context.datastore.data.first()
        //.data를 통해 반환되는 플로우 형태의 데이터
        //해당 플로우에서 방출되는 첫 번째 값, 즉, 현재 저장된 값
        return prefs[AUTO_LOGIN_KEY] ?: false
    }
    
    // 토큰 삭제하는 함수
    override suspend fun deleteToken() {
        context.datastore.edit {
            it.remove(TOKEN_KEY)
        }
    }
}