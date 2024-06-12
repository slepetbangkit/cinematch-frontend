package com.slepetbangkit.cinematch.view.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.ActivityResponse
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.di.Injection
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ActivityViewModel(private val sessionRepository: SessionRepository): ViewModel() {
    private lateinit var accessToken: String

    private val _activity = MutableLiveData<ActivityResponse>()
    val activity: MutableLiveData<ActivityResponse> = _activity

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: MutableLiveData<String> = _error

    init {
        viewModelScope.launch {
            accessToken = sessionRepository.getAccessToken().first()

            fetchActivity()
        }
    }

    private fun fetchActivity() {
        _isLoading.value = true

        val client = Injection.provideApiService().getActivities("Bearer $accessToken")
        client.enqueue(object : retrofit2.Callback<ActivityResponse> {
            override fun onResponse(call: retrofit2.Call<ActivityResponse>, response: retrofit2.Response<ActivityResponse>) {
                if (response.isSuccessful) {
                    _activity.value = response.body()
                } else {
                    _error.value = response.message()
                }

                _isLoading.value = false
            }

            override fun onFailure(call: retrofit2.Call<ActivityResponse>, t: Throwable) {
                _error.value = t.message

                _isLoading.value = false
            }
        })
    }
}