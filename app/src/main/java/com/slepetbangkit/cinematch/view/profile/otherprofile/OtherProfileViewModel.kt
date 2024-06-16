package com.slepetbangkit.cinematch.view.profile.otherprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.ProfileResponse
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

class OtherProfileViewModel(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
    private val username: String
): ViewModel() {
    private val _profile = MutableLiveData<ProfileResponse>()
    val profile: LiveData<ProfileResponse> get() = _profile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        viewModelScope.launch {
            getOtherProfile()
        }
    }

    fun setIsFollowed(isFollowed: Boolean) {
        val oldProfile = _profile.value
        if (oldProfile != null) {
            if (isFollowed) {
                _profile.postValue(oldProfile.copy(isFollowed = isFollowed, followerCount = oldProfile.followerCount + 1))
            } else {
                _profile.postValue(oldProfile.copy(isFollowed = isFollowed, followerCount = oldProfile.followerCount - 1))
            }
        }
    }

    private suspend fun getOtherProfile() {
        try {
            _isLoading.value = true
            val profileResponse = userRepository.getOtherProfile(username)
            _profile.value = profileResponse
        } catch (e: SocketTimeoutException) {
            _error.value = e.message
            getOtherProfile()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                sessionRepository.refresh()
                getOtherProfile()
            } else {
                _error.value = e.message
            }
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun follow() {
        try {
            userRepository.follow(username)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                sessionRepository.refresh()
                follow()
            } else {
                _error.value = e.message()
            }
        }
    }

    suspend fun unfollow() {
        try {
            userRepository.unfollow(username)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                sessionRepository.refresh()
                unfollow()
            } else {
                _error.value = e.message()
            }
        }
    }
}