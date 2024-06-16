package com.slepetbangkit.cinematch.view.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.ActivityResponse
import com.slepetbangkit.cinematch.data.repository.ActivityRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.di.Injection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException

class ActivityViewModel(
    private val sessionRepository: SessionRepository,
    private val activityRepository: ActivityRepository
): ViewModel() {
    private val _activity = MutableLiveData<ActivityResponse>()
    val activity: MutableLiveData<ActivityResponse> = _activity

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: MutableLiveData<String> = _error

    init {
        viewModelScope.launch {
            getActivities()
        }
    }

    private suspend fun getActivities() {
        try {
            _isLoading.value = true
            val response = activityRepository.getActivities()
            _activity.value = response
        } catch (e: SocketTimeoutException) {
            _error.value = e.message
            getActivities()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                withContext(Dispatchers.IO) {
                    sessionRepository.refresh()
                }
                getActivities()
            } else {
                _error.value = e.message
            }
        } finally {
            _isLoading.value = false
        }
    }
}