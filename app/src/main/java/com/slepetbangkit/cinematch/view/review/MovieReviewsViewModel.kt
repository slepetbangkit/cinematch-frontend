package com.slepetbangkit.cinematch.view.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.MovieReviewsResponse
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiConfig
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieReviewsViewModel(private val sessionRepository: SessionRepository, private val movie: Int) : ViewModel(){
    private lateinit var accessToken: String

    private val _tmdbId = MutableLiveData<Int>()
    val tmdbId: LiveData<Int> = _tmdbId

    private val _movieReviews = MutableLiveData<MovieReviewsResponse>()
    val movieReviews: LiveData<MovieReviewsResponse> get() = _movieReviews

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        viewModelScope.launch {
            accessToken = sessionRepository.getAccessToken().first()

            _tmdbId.value = movie

            fetchMovieReviews()
        }
    }

    private fun fetchMovieReviews() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getMovieReviews("Bearer $accessToken",
            tmdbId.value!!.toInt()
        )
        client.enqueue(object : Callback<MovieReviewsResponse> {
            override fun onResponse(call: Call<MovieReviewsResponse>, response: Response<MovieReviewsResponse>) {
                if (response.isSuccessful) {
                    _movieReviews.value = response.body()
                } else {
                    _error.value = response.message()
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<MovieReviewsResponse>, t: Throwable) {
                _error.value = t.message
                _isLoading.value = false
            }
        })
    }
}