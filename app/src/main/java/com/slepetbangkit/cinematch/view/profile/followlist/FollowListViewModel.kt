package com.slepetbangkit.cinematch.view.profile.followlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.FollowListResponse
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.di.Injection
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FollowListViewModel(private val sesisonRepository: SessionRepository, private val username: String): ViewModel() {
    private lateinit var accessToken: String

    private var _followList = MutableLiveData<FollowListResponse>()
    val followList: LiveData<FollowListResponse> = _followList

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private var _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        viewModelScope.launch {
            accessToken = sesisonRepository.getAccessToken().first()

            fetchFollowList()
        }
    }

    private fun fetchFollowList() {
        val client = Injection.provideApiService().getFollowList("Bearer $accessToken", username)
        client.enqueue(object : retrofit2.Callback<FollowListResponse> {
            override fun onResponse(call: retrofit2.Call<FollowListResponse>, response: retrofit2.Response<FollowListResponse>) {
                if (response.isSuccessful) {
                    _followList.value = response.body()
                } else {
                    _error.value = "Login Failed: ${response.message()}"
                }
//                _isLoading.value = false
            }

            override fun onFailure(call: retrofit2.Call<FollowListResponse>, t: Throwable) {
                _error.value = "Login Failed: ${t.message}"
//                _isLoading.value = false
            }
        })
    }
}