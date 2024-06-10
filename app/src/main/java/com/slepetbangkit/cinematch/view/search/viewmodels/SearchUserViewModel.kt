package com.slepetbangkit.cinematch.view.search.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.UserSearchResponse
import com.slepetbangkit.cinematch.data.remote.response.UsersItem
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiConfig
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchUserViewModel(sessionRepository: SessionRepository) : ViewModel() {
    private lateinit var accessToken: String

    private val _searchUserResult = MutableLiveData<List<UsersItem>>()
    val searchUserResult: LiveData<List<UsersItem>> = _searchUserResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        viewModelScope.launch {
            accessToken = sessionRepository.getAccessToken().first()
        }
    }

    fun getSearchUsers(username: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().searchUser("Bearer $accessToken", username)
        client.enqueue(object : Callback<UserSearchResponse> {
            override fun onResponse(call: Call<UserSearchResponse>, response: Response<UserSearchResponse>) {
                if (response.isSuccessful) {
                    _searchUserResult.value = response.body()?.users?.filterNotNull() ?: emptyList()
                } else {
                    _error.value = "Error fetching data"
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                _error.value = "Failed fetching data: ${t.message}"
                _isLoading.value = false
            }
        })
    }
}
