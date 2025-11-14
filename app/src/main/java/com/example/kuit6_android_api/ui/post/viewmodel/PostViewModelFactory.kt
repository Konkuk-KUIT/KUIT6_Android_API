package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.kuit6_android_api.App
import com.example.kuit6_android_api.data.repository.PostRepository

/**
 * Repository 패턴을 위해 Repository를 ViewModel에 파라미터로 전달하는 Factory
 * Repository는 수동 주입(App Container)을 통해 가져오며,
 * ViewModel에 파라미터를 전달하기 위해 Factory 패턴을 사용합니다.
 */
inline fun <reified VM : ViewModel> postViewModelFactory(
    crossinline create: (PostRepository) -> VM
): ViewModelProvider.Factory = viewModelFactory {
    initializer {
        val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                as App

        val postRepository = application.container.postRepository

        create(postRepository)
    }
}