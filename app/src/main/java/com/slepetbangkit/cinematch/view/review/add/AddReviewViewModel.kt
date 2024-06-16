package com.slepetbangkit.cinematch.view.review.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.AddReviewResponse
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiConfig
import com.slepetbangkit.cinematch.data.repository.MovieRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException

class AddReviewViewModel(
    private val sessionRepository: SessionRepository,
    private val movieRepository: MovieRepository,
    private val movie: Int
) : ViewModel() {
    private val _reviewResponse = MutableLiveData<AddReviewResponse?>()
    val reviewResponse: MutableLiveData<AddReviewResponse?> get() = _reviewResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?> = _error

    suspend fun addReview(description: String) {
        try {
            _isLoading.value = true
            val response = movieRepository.addReview(movie, description)
            _reviewResponse.value = response
        } catch (e: SocketTimeoutException) {
            _error.value = "Request timed out. Please try again."
        } catch (e: HttpException) {
            if (e.code() == 401) {
                withContext(Dispatchers.IO) {
                    sessionRepository.refresh()
                }
                addReview(description)
            } else {
                _error.value = e.message
            }
        } finally {
            _isLoading.value = false
        }
    }
}