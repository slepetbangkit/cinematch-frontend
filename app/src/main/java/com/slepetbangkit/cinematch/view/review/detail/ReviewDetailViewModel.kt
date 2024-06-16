package com.slepetbangkit.cinematch.view.review.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.ReviewDetailsResponse
import com.slepetbangkit.cinematch.data.repository.MovieRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

class ReviewDetailViewModel (
    private val sessionRepository: SessionRepository,
    private val movieRepository: MovieRepository,
    private val review: String
) : ViewModel() {
    private val _reviewId = MutableLiveData<String>()
    val reviewId: LiveData<String> = _reviewId

    private val _reviewDetails = MutableLiveData<ReviewDetailsResponse>()
    val reviewDetails: LiveData<ReviewDetailsResponse> get() = _reviewDetails

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        viewModelScope.launch {
            _reviewId.value = review

            getReviewDetails()
        }
    }

    private suspend fun getReviewDetails() {
        try {
            _isLoading.value = true
            val response = movieRepository.getReviewDetailsById(review)
            _reviewDetails.value = response
        } catch (e: SocketTimeoutException) {
            _error.value = e.message
            getReviewDetails()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                sessionRepository.refresh()
                getReviewDetails()
            }
            else {
                _error.value = e.message
            }
        } finally {
            _isLoading.value = false
        }
    }
}
