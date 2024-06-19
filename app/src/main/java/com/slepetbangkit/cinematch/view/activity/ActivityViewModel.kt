package com.slepetbangkit.cinematch.view.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slepetbangkit.cinematch.data.remote.response.ActivityResponse
import com.slepetbangkit.cinematch.data.repository.ActivityRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException

class ActivityViewModel(
    private val sessionRepository: SessionRepository,
    private val activityRepository: ActivityRepository
): ViewModel() {
    private var isFetched = false

    private val _activity = MutableLiveData<ActivityResponse>()
    val activity: MutableLiveData<ActivityResponse> = _activity

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: MutableLiveData<String> = _error

    suspend fun getActivities() {
        try {
            if (!isFetched) {
                _isLoading.value = true
            }
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
            if (!isFetched) {
                _isLoading.value = false
                isFetched = true
            }
        }
    }
}