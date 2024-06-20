package com.slepetbangkit.cinematch.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.RegisterResponse
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

class RegisterViewModel(private val sessionRepository: SessionRepository) : ViewModel() {
    private val _registerResult = MutableLiveData<RegisterResponse>()
    val registerResult: LiveData<RegisterResponse> = _registerResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun register(
        username: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val response = sessionRepository.register(username, email, password)
                _registerResult.value = response
            } catch (e: SocketTimeoutException) {
                _error.value = "Register Failed: ${e.message}"
            } catch (e: HttpException) {
                _error.value = "Register Failed: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}