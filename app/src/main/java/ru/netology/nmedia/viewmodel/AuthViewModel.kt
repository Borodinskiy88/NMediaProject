package ru.netology.nmedia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import ru.netology.nmedia.auth.AppAuth

class AuthViewModel : ViewModel() {

    val data = AppAuth.geiInstance()
        .data
        .asLiveData(Dispatchers.Default)

    val authorized: Boolean
        get() = data.value != null

}