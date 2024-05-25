package com.slepetbangkit.cinematch.view.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.local.preferences.SessionPreferences
import com.slepetbangkit.cinematch.data.remote.response.ProfileResponse
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel(sessionPrefs: SessionPreferences) : ViewModel() {
    private lateinit var accessToken: String

    private val _profile = MutableLiveData<ProfileResponse>()
    val profile: LiveData<ProfileResponse> get() = _profile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        viewModelScope.launch {
            accessToken = sessionPrefs.getAccessToken().first()
            Log.d("ProfileViewModel", "accessToken: $accessToken")
            fetchProfile()
        }
    }

    private fun fetchProfile() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getProfile("Bearer $accessToken")
        client.enqueue(object : retrofit2.Callback<ProfileResponse> {
            override fun onResponse(call: retrofit2.Call<ProfileResponse>, response: retrofit2.Response<ProfileResponse>) {
                if (response.isSuccessful) {
                    _profile.value = response.body()
                } else {
                    _error.value = "Login Failed: ${response.message()}"
                }
                _isLoading.value = false
            }

            override fun onFailure(call: retrofit2.Call<ProfileResponse>, t: Throwable) {
                _error.value = "Login Failed: ${t.message}"
                _isLoading.value = false
            }
        })
    }
}