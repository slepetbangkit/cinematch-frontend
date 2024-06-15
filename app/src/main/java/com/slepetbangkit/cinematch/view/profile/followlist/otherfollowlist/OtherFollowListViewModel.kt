package com.slepetbangkit.cinematch.view.profile.followlist.otherfollowlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.FollowListResponse
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class OtherFollowListViewModel(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
    private val username: String
): ViewModel() {
    private val _followList = MutableLiveData<FollowListResponse>()
    val followList: LiveData<FollowListResponse> = _followList

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            getOtherFollowList()
        }
    }

    suspend fun getOtherFollowList() {
        try {
            _isLoading.value = true
            val response = userRepository.getOtherFollowList(username)
            Log.d("OtherFollowListViewModel", response.toString())
            _followList.value = response
        } catch (e: HttpException) {
            if (e.code() == 401) {
                sessionRepository.refresh()
                getOtherFollowList()
            } else {
                _error.value = e.message()
            }
        } finally {
            _isLoading.value = false
        }
    }
}