package com.slepetbangkit.cinematch.view.review.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.ReviewDetailsResponse
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiConfig
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewDetailViewModel (
    private val sessionRepository: SessionRepository, private val review: String
) : ViewModel() {

    private lateinit var accessToken: String

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
            accessToken = sessionRepository.getAccessToken().first()

            _reviewId.value = review

            fetchReviewData()
        }
    }

    private fun fetchReviewData() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getDetailReviewById("Bearer $accessToken",
            reviewId.value.toString()
        )
        client.enqueue(object : Callback<ReviewDetailsResponse> {
            override fun onResponse(call: Call<ReviewDetailsResponse>, response: Response<ReviewDetailsResponse>) {
                if (response.isSuccessful) {
                    _reviewDetails.value = response.body()
                } else {
                    _error.value = response.message()
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<ReviewDetailsResponse>, t: Throwable) {
                _error.value = t.message
                _isLoading.value = false
            }
        })
    }
}
