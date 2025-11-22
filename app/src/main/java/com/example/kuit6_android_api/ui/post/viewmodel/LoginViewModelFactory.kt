package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.kuit6_android_api.App
import com.example.kuit6_android_api.data.repository.LoginRepository
import com.example.kuit6_android_api.data.repository.TokenRepository

inline fun <reified VM : ViewModel> loginViewModelFactory(
    crossinline create: (LoginRepository, TokenRepository, App) -> VM
    //app 전달
): ViewModelProvider.Factory = viewModelFactory {
    initializer {
        val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                as App
        // 어플리케이션 컨텍스트를 전달하는 것이 안정적

        val loginRepository = application.container.loginRepository
        val tokenRepository = application.container.tokenRepository
        create(loginRepository, tokenRepository, application)
    }
}