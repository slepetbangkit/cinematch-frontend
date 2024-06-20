package com.slepetbangkit.cinematch.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slepetbangkit.cinematch.data.remote.response.HomeResponse
import com.slepetbangkit.cinematch.data.repository.HomeRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class HomeViewModel(
    private val sessionRepository: SessionRepository,
    private val homeRepository: HomeRepository
): ViewModel() {
    private var isFetched: Boolean = false

    private val _response = MutableLiveData<HomeResponse>()
    val response: LiveData<HomeResponse> = _response

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    suspend fun getHomeMovies() {
        try {
            if (!isFetched) {
                _isLoading.value = true
            }
            _response.value = homeRepository.getHomeMovies()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                withContext(Dispatchers.IO) {
                    sessionRepository.refresh()
                }
                getHomeMovies()
            } else {
                _error.value = "An error occurred"
            }
            _error.value = "An error occured"
        } finally {
            if (!isFetched) {
                _isLoading.value = false
                isFetched = true
            }
        }
    }
}