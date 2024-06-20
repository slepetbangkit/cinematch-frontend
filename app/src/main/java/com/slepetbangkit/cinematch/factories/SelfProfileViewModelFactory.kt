package com.slepetbangkit.cinematch.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository
import com.slepetbangkit.cinematch.view.profile.followlist.selffollowlist.SelfFollowListViewModel
import com.slepetbangkit.cinematch.view.profile.selfprofile.SelfProfileViewModel

class SelfProfileViewModelFactory private constructor(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SelfProfileViewModel::class.java) -> {
                SelfProfileViewModel(sessionRepository, userRepository) as T
            }
            modelClass.isAssignableFrom(SelfFollowListViewModel::class.java) -> {
                SelfFollowListViewModel(sessionRepository, userRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SelfProfileViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            sessionRepository: SessionRepository,
            userRepository: UserRepository
        ): SelfProfileViewModelFactory {
            if (INSTANCE == null) {
                synchronized(SelfProfileViewModelFactory::class.java) {
                    INSTANCE = SelfProfileViewModelFactory(sessionRepository, userRepository)
                }
            }
            return INSTANCE as SelfProfileViewModelFactory
        }
    }
}