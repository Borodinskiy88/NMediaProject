//package ru.netology.nmedia.viewmodel
//
//import android.app.Application
//import androidx.lifecycle.*
//import ru.netology.nmedia.dto.Post
//import ru.netology.nmedia.model.FeedModel
//import ru.netology.nmedia.repository.*
//import ru.netology.nmedia.util.SingleLiveEvent
//
//private val empty = Post(
//    id = 0,
//    content = "",
//    author = "",
//    authorAvatar = " ",
//    likes = 0,
//    likedByMe = false,
//    published = "",
//    attachment = null
//)
//
//class PostViewModelCallback(application: Application) : AndroidViewModel(application) {
//    private val repository: PostRepositoryCallback = PostRepositoryCallbackImpl()
//    private val _data = MutableLiveData(FeedModel())
//    val data: LiveData<FeedModel>
//        get() = _data
//    val edited = MutableLiveData(empty)
//    private val _postCreated = SingleLiveEvent<Unit>()
//    val postCreated: LiveData<Unit>
//        get() = _postCreated
//
//    init {
//        loadPosts()
//    }
//
//    fun loadPosts() {
//        _data.value = (FeedModel(loading = true))
//        repository.getAll(object : PostRepositoryCallback.Callback<List<Post>> {
//            override fun onSuccess(posts: List<Post>) {
//                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
//            }
//
//            override fun onError(e: Exception) {
//                _data.postValue(FeedModel(error = true))
//            }
//        })
//    }
//
//    fun save() {
//        edited.value?.let {
//            repository.save(it, object : PostRepositoryCallback.Callback<Post> {
//                override fun onSuccess(value: Post) {
//                    _postCreated.postValue(Unit)
//                }
//
//                override fun onError(e: Exception) {
//                    _postCreated.postValue(Unit)
//                    _data.postValue(FeedModel(error = true))
//                }
//
//            })
//        }
//        edited.value = empty
//    }
//
//    fun edit(post: Post) {
//        edited.value = post
//    }
//
//    fun changeContent(content: String) {
//        val text = content.trim()
//        if (edited.value?.content == text) {
//            return
//        }
//        edited.value = edited.value?.copy(content = text)
//    }
//
//    fun likeById(id: Long) {
//        repository.likeById(id, object : PostRepositoryCallback.Callback<Post> {
//            override fun onSuccess(value: Post) {
//                val posts = _data.value?.posts.orEmpty()
//                    .map {
//                        if (it.id == id) {
//                            value
//                        } else {
//                            it
//                        }
//                    }
//                _data.postValue(_data.value?.copy(posts = posts))
//            }
//
//            override fun onError(e: Exception) {
//                println(e.message.toString())
//            }
//
//        })
//    }
//
//    fun unlikeById(id: Long) {
//        repository.likeById(id, object : PostRepositoryCallback.Callback<Post> {
//            override fun onSuccess(value: Post) {
//                val posts = _data.value?.posts.orEmpty()
//                    .map {
//                        if (it.id == id) {
//                            value
//                        } else {
//                            it
//                        }
//                    }
//                _data.postValue(_data.value?.copy(posts = posts))
//            }
//
//            override fun onError(e: Exception) {
//                println(e.message.toString())
//            }
//
//        })
//    }
//
//    fun removeById(id: Long) {
//        val old = _data.value?.posts.orEmpty()
//        repository.removeById(id, object : PostRepositoryCallback.Callback<Unit> {
//            override fun onSuccess(value: Unit) {
//                _data.postValue(
//                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
//                        .filter { it.id != id }
//                    )
//                )
//            }
//
//            override fun onError(e: Exception) {
//                _data.postValue(_data.value?.copy(posts = old))
//            }
//
//        })
//    }
//}
