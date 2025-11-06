package com.example.kuit6_android_api.data.model.repository

import android.util.Log
import com.example.kuit6_android_api.data.api.ApiService
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.model.response.PostResponse

class PostRepositoryImpl(
    private val apiService: ApiService
) : PostRepository{
    override suspend fun getPost(): Result<List<PostResponse>> {
        return runCatching {
            val response = apiService.getPosts()

            if(response.success && response.data !=null){
                response.data
            }else{
                throw Exception(response.message ?:"게시글 불러오기 실패")
            }
        }.onFailure { error->
            Log.e("PostRepository",error.message.toString())
        }
    }

    override suspend fun createPost(
        author: String,
        title: String,
        content: String,
        imageUrl: String?
    ): Result<PostResponse> {
        return runCatching {
            val response = apiService.createPost("규반", PostCreateRequest(title,content,imageUrl))

            if(response.success && response.data != null){
                response.data
            }else{
                throw Exception(response.message ?:"게시글 생성 실패")
            }
        }.onFailure { error->
            Log.e("PostRepository",error.message.toString())
        }
    }

    override suspend fun getPostDetail(id: Long): Result<PostResponse> {
        return runCatching {
            val response = apiService.getPostDetail(id)

            if(response.success && response.data != null){
                response.data
            }else{
                throw Exception(response.message ?:"게시글 불러오기 실패")
            }
        }.onFailure { error->
            Log.e("PostRepository",error.message.toString())
        }
    }

    override suspend fun deletePost(id: Long): Result<PostResponse> {

        return runCatching {
            val response = apiService.deletePost(id)

            if(response.success && response.data != null){
                response.data
            }else{
                throw Exception(response.message ?:"게시글 삭제 실패")
            }
        }.onFailure { error->
            Log.e("PostRepository",error.message.toString())
        }
    }

    override suspend fun editPost(
        id: Long,
        request: PostCreateRequest
    ): Result<PostResponse> {
        return runCatching {
            val response = apiService.editPost(id,request)

            if(response.success && response.data != null){
                response.data
            }else{
                throw Exception(response.message ?:"게시글 수정 실패")
            }
        }.onFailure { error->
            Log.e("PostRepository",error.message.toString())
        }
    }


}