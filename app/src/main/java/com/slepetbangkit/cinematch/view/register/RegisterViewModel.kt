package com.slepetbangkit.cinematch.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.api.response.RegisterResponse
import com.slepetbangkit.cinematch.data.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _registerResult = MutableLiveData<RegisterResponse>()
    val registerResult: LiveData<RegisterResponse> = _registerResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun register(
        username: String,
        email: String,
        password: String,
        password2: String,
        bio: String
    ) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = userRepository.registerUser(
                    username,
                    email,
                    password,
                    password2,
                    bio
                )
                _registerResult.value = response
            } catch (e: HttpException) {
                _error.value = "Registration Failed: ${e.message()}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}