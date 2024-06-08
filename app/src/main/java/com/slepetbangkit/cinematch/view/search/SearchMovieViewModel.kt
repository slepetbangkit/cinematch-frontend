package com.slepetbangkit.cinematch.view.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.local.preferences.SessionPreferences
import com.slepetbangkit.cinematch.data.remote.response.SearchResponseItem
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiConfig
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchMovieViewModel(sessionRepository: SessionRepository) : ViewModel() {
    private lateinit var accessToken: String

    private val _searchMovieResult = MutableLiveData<List<SearchResponseItem>>()
    val searchMovieResult: LiveData<List<SearchResponseItem>> = _searchMovieResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        viewModelScope.launch {
            accessToken = sessionRepository.getAccessToken().first()
        }
    }

    fun getSearchMovies(movieName: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().searchMovies("Bearer $accessToken", movieName)
        client.enqueue(object : Callback<List<SearchResponseItem>> {
            override fun onResponse(call: Call<List<SearchResponseItem>>, response: Response<List<SearchResponseItem>>) {
                if (response.isSuccessful) {
                    _searchMovieResult.value = response.body()?.filterNotNull() ?: emptyList()
                } else {
                    _error.value = "Error fetching data"
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<List<SearchResponseItem>>, t: Throwable) {
                _error.value = "Failed fetching data: ${t.message}"
                _isLoading.value = false
            }
        })
    }

    fun clearSearchResults() {
        _searchMovieResult.value = emptyList()
    }
}
