package com.slepetbangkit.cinematch.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.view.activity.ActivityViewModel
import com.slepetbangkit.cinematch.view.main.MainViewModel

class MainViewModelFactory private constructor(
    private val sessionRepository: SessionRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(sessionRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: MainViewModelFactory? = null

        @JvmStatic
        fun getInstance(sessionRepository: SessionRepository): MainViewModelFactory {
            if (INSTANCE == null) {
                synchronized(MainViewModel::class.java) {
                    INSTANCE = MainViewModelFactory(sessionRepository)
                }
            }
            return INSTANCE as MainViewModelFactory
        }
    }
}