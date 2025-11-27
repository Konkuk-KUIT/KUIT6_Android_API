package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.kuit6_android_api.App
import com.example.kuit6_android_api.data.repository.PostRepository
import okhttp3.Call

inline fun <reified VM: ViewModel> PostViewModelFactory(
    crossinline create: (PostRepository) -> VM
): ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                as App

            val postRepository = application.container.postRepository

            create(postRepository)
        }
    }
