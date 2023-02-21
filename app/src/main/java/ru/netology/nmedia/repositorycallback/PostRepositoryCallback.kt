//package ru.netology.nmedia.repository
//
//import ru.netology.nmedia.dto.Post
//
//interface PostRepositoryCallback {
//    fun getAll(callback: Callback<List<Post>>)
//    fun likeById(id: Long, callback: Callback<Post>)
//    fun unlikeById(id: Long, callback: Callback<Post>)
//    fun removeById(id: Long, callback: Callback<Unit>)
//    fun save(post: Post, callback: Callback<Post>)
//
//    interface Callback<T> {
//        fun onSuccess(value: T)
//        fun onError(e: Exception)
//    }
//}
