package com.slepetbangkit.cinematch.view.profile.followlist.selffollowlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.FollowListResponse
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

class SelfFollowListViewModel(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
): ViewModel() {
    private var isFetched = false

    private val _followList = MutableLiveData<FollowListResponse>()
    val followList: LiveData<FollowListResponse> = _followList

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    suspend fun getSelfFollowList() {
        try {
            if (!isFetched) {
                _isLoading.value = true
            }
            val response = userRepository.getSelfFollowList()
            _followList.value = response
        } catch (e: SocketTimeoutException) {
            _error.value = e.message
            getSelfFollowList()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                sessionRepository.refresh()
                getSelfFollowList()
            } else {
                _error.value = e.message
            }
        } finally {
            if (!isFetched) {
                isFetched = true
                _isLoading.value = false
            }
        }
    }
}