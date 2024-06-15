package com.slepetbangkit.cinematch.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository
import com.slepetbangkit.cinematch.view.search.viewmodels.SearchUserViewModel

class SearchUserViewModelFactory private constructor(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SearchUserViewModel::class.java) -> {
                SearchUserViewModel(sessionRepository, userRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SearchUserViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            sessionRepository: SessionRepository,
            userRepository: UserRepository
        ): SearchUserViewModelFactory {
            if (INSTANCE == null) {
                synchronized(SearchUserViewModelFactory::class.java) {
                    INSTANCE = SearchUserViewModelFactory(sessionRepository, userRepository)
                }
            }
            return INSTANCE as SearchUserViewModelFactory
        }
    }
}