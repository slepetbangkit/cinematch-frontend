package com.slepetbangkit.cinematch.view.search.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.UserSearchResponse
import com.slepetbangkit.cinematch.data.remote.response.UsersItem
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiConfig
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException

class SearchUserViewModel(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _searchUserResult = MutableLiveData<List<UsersItem>>()
    val searchUserResult: LiveData<List<UsersItem>> = _searchUserResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    suspend fun searchUser(username: String) {
        try {
            _isLoading.value = true
            val response = userRepository.searchUser(username)
            _searchUserResult.value = response.users
        } catch (e: SocketTimeoutException) {
            _error.value = e.message
            searchUser(username)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                sessionRepository.refresh()
                searchUser(username)
            } else {
                _error.value = e.message
            }
        } finally {
            _isLoading.value = false
        }
    }
}
