package com.slepetbangkit.cinematch.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.slepetbangkit.cinematch.data.remote.response.LoginResponse
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiConfig
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException

class LoginViewModel(private val sessionRepository: SessionRepository): ViewModel() {
    private val _loginResult = MutableLiveData<LoginResponse>()
    val loginResult: LiveData<LoginResponse> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val response = sessionRepository.login(username, password)
                _loginResult.value = response
            } catch (e: SocketTimeoutException) {
                _error.value = "Login Failed: ${e.message}"
            } catch (e: HttpException) {
                _error.value = "Login Failed: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}