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

class ProfileViewModel(private val sessionRepository: SessionRepository, private val selectedUsername: String) : ViewModel() {
    private lateinit var accessToken: String
    private lateinit var username: String

//    private val _username = MutableLiveData<String>()
//    val username: LiveData<String> = _username

    private val _isOwnProfile = MutableLiveData<Boolean>()
    val isOwnProfile: LiveData<Boolean> = _isOwnProfile

    private val _isProfileUpdated: MutableLiveData<Boolean> = MutableLiveData(false)
    val isProfileUpdated get() = _isProfileUpdated

    private val _profile = MutableLiveData<ProfileResponse>()
    val profile: LiveData<ProfileResponse> get() = _profile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        viewModelScope.launch {
            accessToken = sessionRepository.getAccessToken().first()
            val ownProfile = selectedUsername.isBlank() || selectedUsername == sessionRepository.getUsername().first()

            if (ownProfile) {
                username = sessionRepository.getUsername().first()
                _isOwnProfile.value = true
            } else {
                username = selectedUsername
                _isOwnProfile.value = false
            }


            fetchProfile()
        }
    }

    fun setProfileUpdated(isUpdated: Boolean) {
        _isProfileUpdated.value = isUpdated
    }

    fun fetchProfile() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getProfile(
            "Bearer $accessToken",
            username
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

    fun followUser() {
        _isLoading.value = true

        val client = Injection.provideApiService().followUser(
            "Bearer $accessToken",
            username
        )
        client.enqueue(object : retrofit2.Callback<MessageResponse> {
            override fun onResponse(call: retrofit2.Call<MessageResponse>, response: retrofit2.Response<MessageResponse>) {
                if (!response.isSuccessful) {
                    _error.value = "Follow Failed: ${response.message()}"
                }
                _isLoading.value = false
            }

            override fun onFailure(call: retrofit2.Call<MessageResponse>, t: Throwable) {
                _error.value = "Follow Failed: ${t.message}"
                _isLoading.value = false
            }
        })
    }

    fun unfollowUser() {
        _isLoading.value = true

        val client = Injection.provideApiService().unfollowUser(
            "Bearer $accessToken",
            username
        )
        client.enqueue(object : retrofit2.Callback<MessageResponse> {
            override fun onResponse(call: retrofit2.Call<MessageResponse>, response: retrofit2.Response<MessageResponse>) {
                if (!response.isSuccessful) {
                    _error.value = "Unfollow Failed: ${response.message()}"
                }
                _isLoading.value = false
            }

            override fun onFailure(call: retrofit2.Call<MessageResponse>, t: Throwable) {
                _error.value = "Unfollow Failed: ${t.message}"
                _isLoading.value = false
            }
        })
    }
}