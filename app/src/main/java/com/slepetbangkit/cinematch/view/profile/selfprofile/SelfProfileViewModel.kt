package com.slepetbangkit.cinematch.view.profile.selfprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.ProfileResponse
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SelfProfileViewModel(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _profile = MutableLiveData<ProfileResponse>()
    val profile: LiveData<ProfileResponse> get() = _profile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        viewModelScope.launch {
            getSelfProfile()
        }
    }

    fun setProfileImage(newProfileImageUrl: String) {
        val oldProfile = _profile.value
        if (oldProfile != null) {
            _profile.postValue(oldProfile.copy(profilePicture = newProfileImageUrl))
        }
    }

    fun setProfileUsername(newUsername: String) {
        val oldProfile = _profile.value
        if (oldProfile != null) {
            _profile.postValue(oldProfile.copy(username = newUsername))
        }
    }

    fun setProfileBio(newBio: String) {
        val oldProfile = _profile.value
        if (oldProfile != null) {
            _profile.postValue(oldProfile.copy(bio = newBio))
        }
    }

    fun incrementFollowingCount() {
        val oldProfile = _profile.value
        if (oldProfile != null) {
            _profile.postValue(oldProfile.copy(followingCount = oldProfile.followingCount + 1))
        }
    }

    fun decrementFollowingCount() {
        val oldProfile = _profile.value
        if (oldProfile != null && oldProfile.followingCount > 0) {
            _profile.postValue(oldProfile.copy(followingCount = oldProfile.followingCount - 1))
        }
    }

    suspend fun getSelfProfile() {
        try {
            _isLoading.value = true
            val profileResponse = userRepository.getSelfProfile()
            _profile.value = profileResponse
        } catch (e: HttpException) {
            if (e.code() == 401) {
                sessionRepository.refresh()
                getSelfProfile()
            } else {
                _error.value = e.message
            }
        } finally {
            _isLoading.value = false
        }
    }
}