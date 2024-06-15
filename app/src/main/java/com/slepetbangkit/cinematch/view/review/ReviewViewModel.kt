package com.slepetbangkit.cinematch.view.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.MovieReviewsResponse
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiConfig
import com.slepetbangkit.cinematch.data.repository.MovieRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class ReviewViewModel(
    private val sessionRepository: SessionRepository,
    private val movieRepository: MovieRepository,
    private val movie: Int
) : ViewModel() {
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
            _tmdbId.value = movie

            getReviews()
        }
    }

    fun getMovieReviews() {
        viewModelScope.launch {
            getReviews()
        }
    }

    private suspend fun getReviews() {
        try {
            _isLoading.value = true
            val response = movieRepository.getMovieReviews(movie)
            _movieReviews.value = response
        } catch (e: HttpException) {
            if (e.code() == 401) {
                sessionRepository.refresh()
                getReviews()
            } else {
                _error.value = "Error fetching data"
            }
        } finally {
            _isLoading.value = false
        }
    }
}
