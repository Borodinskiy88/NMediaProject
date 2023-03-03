package ru.netology.nmedia.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.model.AuthModel
import ru.netology.nmedia.model.AuthModelState
import ru.netology.nmedia.model.MediaModel
import ru.netology.nmedia.repository.AuthRepository
import ru.netology.nmedia.repository.AuthRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.File

class RegistrationViewModel : ViewModel() {
    private val repository: AuthRepository = AuthRepositoryImpl()

    private val _state = MutableLiveData<AuthModelState>()
    val state: LiveData<AuthModelState>
        get() = _state

    private val _media = MutableLiveData<MediaModel?>(null)
    val media: LiveData<MediaModel?>
        get() = _media

    //todo
    private val _registrationSuccess = SingleLiveEvent<AuthModel>()
    val registrationSuccess: LiveData<AuthModel>
        get() = _registrationSuccess

    val data: LiveData<AuthModel?> = AppAuth.geiInstance()
        .data
        .asLiveData(Dispatchers.Default)

    fun addPhoto(uri: Uri, file: File) {
        _media.value = MediaModel(uri, file)
    }

    fun register(login: String, pass: String, repeatPass: String, name: String) = viewModelScope.launch {
        if (login.isNotBlank() && pass.isNotBlank() && name.isNotBlank()) {
            val avatar = media.value
            if (avatar != null) {
                try {
                    _state.value = AuthModelState(loading = true)
                    val result = repository.registerWithPhoto(login, pass, name, avatar)
                    AppAuth.geiInstance().setAuth(result.id, result.token)
                    _state.value = AuthModelState(successfulEntry = true)
                } catch (e: Exception) {
                    _state.value = AuthModelState(error = true)
                }

            } else {
                try {
                    _state.value = AuthModelState(loading = true)
                    val result = repository.register(login, pass, name)
                    AppAuth.geiInstance().setAuth(result.id, result.token)
                    _state.value = AuthModelState(successfulEntry = true)
                } catch (e: Exception) {
                    _state.value = AuthModelState(error = true)
                }
            }
        } else {
            _state.value = AuthModelState(isBlank = true)
        }
        _state.value = AuthModelState()
        clearPhoto()
    }

    fun clearPhoto() {
        _media.value = null
    }


}