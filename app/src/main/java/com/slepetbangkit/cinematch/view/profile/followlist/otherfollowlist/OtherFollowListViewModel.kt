package com.slepetbangkit.cinematch.view.profile.followlist.otherfollowlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.FollowListResponse
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException

class OtherFollowListViewModel(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
    private val username: String
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

    suspend fun getOtherFollowList() {
        try {
            if (!isFetched) {
                _isLoading.value = true
            }
            val response = userRepository.getOtherFollowList(username)
            _followList.value = response
        } catch (e: SocketTimeoutException) {
            _error.value = e.message
            getOtherFollowList()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                withContext(Dispatchers.IO) {
                    sessionRepository.refresh()
                }
                getOtherFollowList()
            } else {
                _error.value = e.message
            }
        } finally {
            if (!isFetched) {
                _isLoading.value = false
                isFetched = true
            }
        }
    }
}