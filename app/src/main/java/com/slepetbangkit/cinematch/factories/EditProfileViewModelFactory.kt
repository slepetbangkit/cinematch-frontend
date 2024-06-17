package com.slepetbangkit.cinematch.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository
import com.slepetbangkit.cinematch.view.profile.editprofile.EditProfileViewModel

class EditProfileViewModelFactory private constructor(
    private val application: Application,
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(EditProfileViewModel::class.java) -> {
                EditProfileViewModel(application, sessionRepository, userRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: EditProfileViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            application: Application,
            sessionRepository: SessionRepository,
            userRepository: UserRepository
        ): EditProfileViewModelFactory {
            if (INSTANCE == null) {
                synchronized(EditProfileViewModelFactory::class.java) {
                    INSTANCE = EditProfileViewModelFactory(application, sessionRepository, userRepository)
                }
            }
            return INSTANCE as EditProfileViewModelFactory
        }
    }
}