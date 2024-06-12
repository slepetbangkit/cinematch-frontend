package com.slepetbangkit.cinematch.view.moviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.MovieDetailResponse
import com.slepetbangkit.cinematch.data.remote.response.ProfileResponse
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiConfig
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel(private val sessionRepository: SessionRepository, private val selectedMovie: Int) : ViewModel() {
    private lateinit var accessToken: String

    private val _tmdbId = MutableLiveData<Int>()
    val tmdbId: LiveData<Int> = _tmdbId

    private val _movieDetail = MutableLiveData<MovieDetailResponse>()
    val movieDetail: LiveData<MovieDetailResponse> get() = _movieDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        viewModelScope.launch {
            accessToken = sessionRepository.getAccessToken().first()

            _tmdbId.value = selectedMovie

            fetchMovieDetail()
        }
    }

    private fun fetchMovieDetail() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getMovieDetail("Bearer $accessToken",
            tmdbId.value!!.toInt()
        )
        client.enqueue(object : Callback<MovieDetailResponse> {
            override fun onResponse(call: Call<MovieDetailResponse>, response: Response<MovieDetailResponse>) {
                if (response.isSuccessful) {
                    _movieDetail.value = response.body()
                } else {
                    _error.value = response.message()
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {
                _error.value = t.message
                _isLoading.value = false
            }
        })
    }

}