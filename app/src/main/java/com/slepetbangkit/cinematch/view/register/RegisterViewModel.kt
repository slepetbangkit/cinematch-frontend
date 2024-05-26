package com.slepetbangkit.cinematch.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.slepetbangkit.cinematch.data.remote.response.ErrorMessage
import com.slepetbangkit.cinematch.data.remote.response.LoginResponse
import com.slepetbangkit.cinematch.data.remote.response.RegisterResponse
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel() : ViewModel() {
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
        _isLoading.value = true

        val client = ApiConfig.getApiService().register(username, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.body()?.error == false) {
                    _registerResult.value = response.body()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val gson = Gson()
                    val errorMessage = gson.fromJson(errorBody, RegisterResponse::class.java).message
                    if (errorMessage is Map<*, *>) {
                        val errorDetails = gson.fromJson(gson.toJson(errorMessage), ErrorMessage::class.java)
                        _error.value = when {
                            !errorDetails.username.isNullOrEmpty() -> errorDetails.username.joinToString(", ")
                            !errorDetails.email.isNullOrEmpty() -> errorDetails.email.joinToString(", ")
                            !errorDetails.password.isNullOrEmpty() -> errorDetails.password.joinToString(", ")
                            else -> "Unknown error"
                        }
                    } else {
                        _error.value = response.body().toString()
                    }
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _error.value = "Register failed: ${t.message}"
                _isLoading.value = false
            }
        })
    }
}