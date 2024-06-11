package com.slepetbangkit.cinematch.helpers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.view.profile.ProfileViewModel
import com.slepetbangkit.cinematch.view.search.viewmodels.SearchMovieViewModel
import com.slepetbangkit.cinematch.view.search.viewmodels.SearchUserViewModel

class ViewModelFactory private constructor(private val sessionRepository: SessionRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
//            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
//                ProfileViewModel(sessionRepository) as T
//            }
            modelClass.isAssignableFrom(SearchMovieViewModel::class.java) -> {
                SearchMovieViewModel(sessionRepository) as T
            }
            modelClass.isAssignableFrom(SearchUserViewModel::class.java) -> {
                SearchUserViewModel(sessionRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(sessionRepository: SessionRepository): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(sessionRepository)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}