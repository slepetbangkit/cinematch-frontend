package com.slepetbangkit.cinematch.view.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.MovieReviewsResponse
import com.slepetbangkit.cinematch.data.repository.MovieRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException

class ReviewViewModel(
    private val sessionRepository: SessionRepository,
    private val movieRepository: MovieRepository,
    private val movie: Int
) : ViewModel() {
    private val _movieReviews = MutableLiveData<MovieReviewsResponse>()
    val movieReviews: LiveData<MovieReviewsResponse> get() = _movieReviews

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isReviewed = MutableLiveData<Boolean>()
    val isReviewed: LiveData<Boolean> = _isReviewed

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

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
            _isReviewed.value = response.isReviewed
        } catch (e: SocketTimeoutException) {
            _error.value = "Error fetching data"
            getReviews()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                withContext(Dispatchers.IO) {
                    sessionRepository.refresh()
                }
                getReviews()
            } else {
                _error.value = "Error fetching data"
            }
        } finally {
            _isLoading.value = false
        }
    }
}
