package com.slepetbangkit.cinematch.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.view.login.LoginViewModel
import com.slepetbangkit.cinematch.view.register.RegisterViewModel

class AuthViewModelFactory private constructor(private val sessionRepository: SessionRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(sessionRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(sessionRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthViewModelFactory? = null

        @JvmStatic
        fun getInstance(sessionRepository: SessionRepository): AuthViewModelFactory {
            if (INSTANCE == null) {
                synchronized(AuthViewModelFactory::class.java) {
                    INSTANCE = AuthViewModelFactory(sessionRepository)
                }
            }
            return INSTANCE as AuthViewModelFactory
        }
    }
}