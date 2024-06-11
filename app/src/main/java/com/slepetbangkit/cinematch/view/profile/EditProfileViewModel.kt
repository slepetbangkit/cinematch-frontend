package com.slepetbangkit.cinematch.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.MessageResponse
import com.slepetbangkit.cinematch.data.remote.response.ProfileResponse
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiConfig
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.di.Injection
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditProfileViewModel(private val sessionRepository: SessionRepository) : ViewModel() {
    private lateinit var accessToken: String

    private val _newUsername = MutableLiveData<String>()
    private val newUsername: LiveData<String> = _newUsername

    private val _newBio = MutableLiveData<String>()
    private val newBio: LiveData<String> = _newBio

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _profile = MutableLiveData<ProfileResponse>()
    val profile: LiveData<ProfileResponse> get() = _profile

    private val _message = MutableLiveData<MessageResponse>()
    val message: LiveData<MessageResponse> get() = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        viewModelScope.launch {
            accessToken = sessionRepository.getAccessToken().first()
            _username.value = sessionRepository.getUsername().first()

            fetchProfile()
        }
    }

    fun setNewUsername(newUsername: String) {
        _newUsername.value = newUsername
    }

    fun setNewBio(newBio: String) {
        _newBio.value = newBio
    }

    private fun fetchProfile() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getProfile(
            "Bearer $accessToken",
            username.value.toString()
        )
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

    fun updateProfile() {
        _isLoading.value = true

        val client = Injection.provideApiService().updateProfile(
            "Bearer $accessToken",
            username.value.toString(),
            newUsername.value.toString(),
            newBio.value.toString()
        )
        client.enqueue(object : retrofit2.Callback<MessageResponse> {
            override fun onResponse(call: retrofit2.Call<MessageResponse>, response: retrofit2.Response<MessageResponse>) {
                if (response.isSuccessful) {
                    _message.value = response.body()
                } else {
                    _error.value = "Update Profile Failed: ${response.message()}"
                }
                _isLoading.value = false
            }

            override fun onFailure(call: retrofit2.Call<MessageResponse>, t: Throwable) {
                _error.value = "Update Profile Failed: ${t.message}"
                _isLoading.value = false
            }
        })
    }
}