package com.slepetbangkit.cinematch.view.profile.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.MessageResponse
import com.slepetbangkit.cinematch.data.remote.response.ProfileResponse
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiConfig
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository
import com.slepetbangkit.cinematch.di.Injection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException

class EditProfileViewModel(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _message = MutableLiveData<MessageResponse>()
    val message: LiveData<MessageResponse> get() = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    suspend fun updateSelfProfile(newUsername: String, newBio: String) {
        try {
            _isLoading.value = true
            val response = userRepository.updateSelfProfile(newUsername, newBio)
            _message.value = response
        } catch (e: SocketTimeoutException) {
            _error.value = e.message
        } catch (e: HttpException) {
            if (e.code() == 401) {
                withContext(Dispatchers.IO) {
                    sessionRepository.refresh()
                }
                updateSelfProfile(newUsername, newBio)
            } else {
                _error.value = e.message
            }
        } finally {
            _isLoading.value = false
        }
    }
}