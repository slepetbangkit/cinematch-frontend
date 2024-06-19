package com.slepetbangkit.cinematch.view.profile.editprofile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slepetbangkit.cinematch.data.remote.response.MessageResponse
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository
import com.slepetbangkit.cinematch.util.reduceFileImage
import com.slepetbangkit.cinematch.util.uriToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.net.SocketTimeoutException

class EditProfileViewModel(
    private val application: Application,
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _currentImageUri: MutableLiveData<Uri?> = MutableLiveData()
    val currentImageUri: LiveData<Uri?> = _currentImageUri

    private val _message = MutableLiveData<MessageResponse>()
    val message: LiveData<MessageResponse> get() = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getResultGallery(uri: Uri?) {
        if (uri != null) {
            _currentImageUri.value = uri
        }
    }

    suspend fun updateSelfProfile(newUsername: String?, newBio: String?) {
        if (_currentImageUri.value == null && newUsername == null && newBio == null) {
            _message.value = MessageResponse(false, "No changes detected")
            return
        }

        try {
            _isLoading.value = true

            val bioRequestBody = newBio?.toRequestBody("text/plain".toMediaType())
            val unameRequestBody = newUsername?.toRequestBody("text/plain".toMediaType())
            val profPicRequestPart = buildImagePart(_currentImageUri.value)

            val response = userRepository.updateSelfProfile(unameRequestBody, bioRequestBody, profPicRequestPart)
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

    private fun buildImagePart(uri: Uri?): MultipartBody.Part? {
        if (uri == null) return null

        _currentImageUri.value.let {
            val imageFile = uriToFile(uri, application).reduceFileImage()
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "profile_picture",
                imageFile.name,
                requestImageFile
            )
            return multipartBody
        }
    }
}