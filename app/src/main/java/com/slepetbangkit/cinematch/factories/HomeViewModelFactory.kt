package com.slepetbangkit.cinematch.factories

import androidx.lifecycle.ViewModelProvider
import com.slepetbangkit.cinematch.data.repository.HomeRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.view.home.HomeViewModel

class HomeViewModelFactory private constructor(
    private val sessionRepository: SessionRepository,
    private val homeRepository: HomeRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(sessionRepository, homeRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: HomeViewModelFactory? = null

        fun getInstance(
            sessionRepository: SessionRepository,
            homeRepository: HomeRepository
        ): HomeViewModelFactory {
            if (INSTANCE == null) {
                synchronized(HomeViewModelFactory::class.java) {
                    INSTANCE = HomeViewModelFactory(sessionRepository, homeRepository)
                }
            }
            return INSTANCE as HomeViewModelFactory
        }
    }
}