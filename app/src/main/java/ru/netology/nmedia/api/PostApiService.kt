package ru.netology.nmedia.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.*
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.dto.Post

private const val BASE_URL = "${BuildConfig.BASE_URL}/api/slow/"
//private const val BASE_URL = "http://10.0.2.2:9999/api/slow/"

private val logging = HttpLoggingInterceptor().apply {
    if (BuildConfig.DEBUG) {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

private val okhttp = OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(okhttp)
    .build()


interface PostsApiService {

    @GET("posts")
    fun getAll(): Call<List<Post>>

    @POST("posts")
    fun save(@Body body: Post): Call<Post>

    @DELETE("posts/{id}")
    fun removeById(@Path("id")postId: Long): Call<Unit>

    @POST("posts/{id}/likes")
    fun likeById(@Path("id")id: Long): Call<Post>

    @POST("posts/{id}/likes")
    fun dislikeById(@Path("id")id: Long): Call<Post>

}

object PostApi {
    val service: PostsApiService by lazy { retrofit.create(PostsApiService::class.java) }
}