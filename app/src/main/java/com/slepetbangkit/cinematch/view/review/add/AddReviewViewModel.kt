package com.slepetbangkit.cinematch.view.review.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.AddReviewResponse
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiConfig
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddReviewViewModel(private val sessionRepository: SessionRepository, private val movie: Int) : ViewModel() {
    private lateinit var accessToken: String

    private val _tmdbId = MutableLiveData<Int>()
    val tmdbId: LiveData<Int> = _tmdbId

    private val _reviewResponse = MutableLiveData<AddReviewResponse?>()
    val reviewResponse: MutableLiveData<AddReviewResponse?> get() = _reviewResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?> = _error

    init {
        viewModelScope.launch {
            accessToken = sessionRepository.getAccessToken().first()
            _tmdbId.value = movie
        }
    }
    fun addReview(description: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().addReview(
            "Bearer $accessToken", _tmdbId.value!!.toInt(), description
        )
        client.enqueue(object : Callback<AddReviewResponse> {
            override fun onResponse(
                call: Call<AddReviewResponse>,
                response: Response<AddReviewResponse>
            ) {
                if (response.isSuccessful) {
                    _reviewResponse.value = response.body()
                    _error.value = null
                } else {
                    _error.value = response.message()
                    _reviewResponse.value = null
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<AddReviewResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = t.message
                _reviewResponse.value = null
            }
        })
    }
}