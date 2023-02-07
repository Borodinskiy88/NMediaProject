package ru.netology.nmedia.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.dto.Post
import java.io.IOException

class PostRepositoryImpl : PostRepository {
    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {

        PostApi.service.getAll()
            .enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    val posts = response.body()
                    if(posts == null) {
                        callback.onError(RuntimeException("body is null"))
                        return
                    }
                    callback.onSuccess(posts)
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    callback.onError(Exception(t))
                }

            })
    }

    override fun likeById(id: Long, callback: PostRepository.Callback<Post>) {

        PostApi.service.likeById(id)
            .enqueue(object : Callback<Post>{
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    val posts = response.body()
                    if(posts == null) {
                        callback.onError(RuntimeException("body is null"))
                        return
                    }
                    callback.onSuccess(posts)
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t))
                }

            })
    }

    override fun dislikeById(id: Long, callback: PostRepository.Callback<Post>) {
        PostApi.service.dislikeById(id)
            .enqueue(object : Callback<Post>{
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    val posts = response.body()
                    if(posts == null) {
                        callback.onError(RuntimeException("body is null"))
                        return
                    }
                    callback.onSuccess(posts)
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t))
                }

            })
    }

    override fun save(post: Post, callback: PostRepository.Callback<Post>) {
        PostApi.service.save(post)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    val posts = response.body()
                    if(posts == null) {
                        callback.onError(RuntimeException("body is null"))
                        return
                    }
                    callback.onSuccess(posts)
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t))
                }

            })
    }

    override fun removeById(id: Long, callback: PostRepository.Callback<Unit>) {
        PostApi.service.removeById(id)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    callback.onSuccess(Unit)
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callback.onError(Exception(t))
                }

            })
    }
}

//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import okhttp3.*
//import okhttp3.MediaType.Companion.toMediaType
//import okhttp3.RequestBody.Companion.toRequestBody
//import okhttp3.internal.EMPTY_REQUEST
//import ru.netology.nmedia.api.PostApi
//import ru.netology.nmedia.dto.Post
//import java.io.IOException
//import java.util.concurrent.TimeUnit

//class PostRepositoryImpl : PostRepository {
//    private val client = OkHttpClient.Builder()
//        .connectTimeout(30, TimeUnit.SECONDS)
//        .build()
//    private val gson = Gson()
//    private val typeToken = object : TypeToken<List<Post>>() {}
//
//    companion object {
//        private const val BASE_URL = "http://10.0.2.2:9999"
//        private val jsonType = "application/json".toMediaType()
//    }
//
//    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {
//        val request: Request = Request.Builder()
//            .url("${BASE_URL}/api/slow/posts")
//            .build()
//
//        client.newCall(request)
//            .enqueue(object : Callback {
//
//                override fun onFailure(call: Call, e: IOException) {
//                    callback.onError(e)
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    try {
//                        val body = response.body?.string() ?: throw RuntimeException("body is null")
//                        callback.onSuccess(gson.fromJson(body, typeToken.type))
//                    } catch (e: Exception) {
//                        callback.onError(e)
//                    }
//                }
//            })
//    }
//
//    override fun likeById(id: Long, callback: PostRepository.Callback<Post>) {
//
//        val request: Request = Request.Builder()
//            .url("${BASE_URL}/api/slow/posts/${id}/likes")
//            .post(EMPTY_REQUEST)
//            .build()
//
//        client.newCall(request)
//            .enqueue(object : Callback {
//
//                override fun onFailure(call: Call, e: IOException) {
//                    callback.onError(e)
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    try {
//                        val body = response.body?.string() ?: throw RuntimeException("body is null")
//                        callback.onSuccess(gson.fromJson(body, Post::class.java))
//                    } catch (e: Exception) {
//                        callback.onError(e)
//                    }
//                }
//            })
//    }
//
//    override fun dislikeById(id: Long, callback: PostRepository.Callback<Post>) {
//        val request: Request = Request.Builder()
//            .url("${BASE_URL}/api/slow/posts/${id}/likes")
//            .delete()
//            .build()
//
//        client.newCall(request)
//            .enqueue(object : Callback {
//
//                override fun onFailure(call: Call, e: IOException) {
//                    callback.onError(e)
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    try {
//                        val body = response.body?.string() ?: throw RuntimeException("body is null")
//                        callback.onSuccess(gson.fromJson(body, Post::class.java))
//                    } catch (e: Exception) {
//                        callback.onError(e)
//                    }
//                }
//            })
//    }
//
//    override fun save(post: Post, callback: PostRepository.Callback<Post>) {
//        val request: Request = Request.Builder()
//            .post(gson.toJson(post).toRequestBody(jsonType))
//            .url("${BASE_URL}/api/slow/posts")
//            .build()
//
//        client.newCall(request)
//            .enqueue(object : Callback {
//
//                override fun onFailure(call: Call, e: IOException) {
//                    callback.onError(e)
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    val body = response.body?.string() ?: throw RuntimeException("body is null")
//                    try {
//                        callback.onSuccess(gson.fromJson(body, Post::class.java))
//                    } catch (e: Exception) {
//                        callback.onError(e)
//                    }
//                }
//            })
//    }
//
//    override fun removeById(id: Long, callback: PostRepository.Callback<Unit>) {
//        val request: Request = Request.Builder()
//            .delete()
//            .url("${BASE_URL}/api/slow/posts/$id")
//            .build()
//
//        client.newCall(request)
//            .enqueue(object : Callback {
//
//                override fun onFailure(call: Call, e: IOException) {
//                    callback.onError(e)
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    try {
//                        callback.onSuccess(Unit)
//                    } catch (e: Exception) {
//                        callback.onError(e)
//                    }
//                }
//            })
//    }
//}
