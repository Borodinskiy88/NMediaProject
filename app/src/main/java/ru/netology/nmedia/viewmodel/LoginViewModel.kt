package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.model.AuthModelState
import ru.netology.nmedia.repository.AuthRepository
import ru.netology.nmedia.repository.AuthRepositoryImpl

class LoginViewModel : ViewModel() {
    private val repository: AuthRepository = AuthRepositoryImpl()

    private val _state = MutableLiveData<AuthModelState>()
    val state: LiveData<AuthModelState>
        get() = _state

    fun login(login: String, password: String) = viewModelScope.launch {
        if (login.isNotBlank() && password.isNotBlank()) {
            try {
                _state.value = (AuthModelState(loading = true))
                val result = repository.login(login, password)
                AppAuth.geiInstance().setAuth(result.id, result.token)
                _state.value = AuthModelState(successfulEntry = true)
            } catch (e: Exception) {
                when (e) {
                    is ApiError -> if (e.status == 404) _state.value =
                        AuthModelState(invalidLoginOrPassword = true)
                    else -> _state.value = AuthModelState(error = true)
                }
            }
        } else {
            _state.value = AuthModelState(isBlank = true)
        }
        _state.value = AuthModelState()
    }
//
//    fun logout() {
//        AppAuth.geiInstance().removeAuth()
//        _state.value = AuthModelState(failedEntry = true)
//    }
}

