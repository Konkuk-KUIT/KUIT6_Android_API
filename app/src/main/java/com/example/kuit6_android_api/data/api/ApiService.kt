package com.example.kuit6_android_api.data.api


import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.model.response.BaseResponse
import com.example.kuit6_android_api.data.model.response.PostResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // 게시글 목록 조회
    // http://3.34.136.227:8080/api/posts
    @GET(value = "/api/posts")
    suspend fun getPosts(): BaseResponse<List<PostResponse>> // suspend fun: 일시 중단 될 수도 있는 함수

    // 게시글 생성
    // http://3.34.136.227:8080/api/posts
    @POST(value = "/api/posts")
    suspend fun createPost(
        @Query(value = "author") author: String = "anonymous",
        @Body request: PostCreateRequest
    ): BaseResponse<PostResponse>

    // 게시글 상세 조회
    @GET(value = "/api/posts/{id}")
    suspend fun getDetail(
        @Path("id") id: Long
    ): BaseResponse<PostResponse>

    // 게시글 수정 [스낵바 - 6주차 미션]
    @PUT(value = "/api/posts/{id}")
    suspend fun updatePost(
        @Path(value = "id") id: Long,
        @Body request: PostCreateRequest
    ): BaseResponse<PostResponse>

    // 게시글 삭제 [스낵바 - 6주차 미션]
    @DELETE(value = "/api/posts/{id}")
    suspend fun deletePost(
        @Path("id") id: Long
    ): BaseResponse<Unit> // 서버가 data 없으면 Unit/Any? 로 받기

    // 이미지 업로드
    @Multipart
    @POST("/api/images/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): BaseResponse<Map<String, String>>
}