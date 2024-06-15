package com.slepetbangkit.cinematch.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository
import com.slepetbangkit.cinematch.view.profile.followlist.otherfollowlist.OtherFollowListViewModel
import com.slepetbangkit.cinematch.view.profile.otherprofile.OtherProfileViewModel

class OtherProfileViewModelFactory private constructor(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
    private val username: String
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(OtherProfileViewModel::class.java) -> {
                OtherProfileViewModel(sessionRepository, userRepository, username) as T
            }
            modelClass.isAssignableFrom(OtherFollowListViewModel::class.java) -> {
                OtherFollowListViewModel(sessionRepository, userRepository, username) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: OtherProfileViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            sessionRepository: SessionRepository,
            userRepository: UserRepository,
            username: String
        ): OtherProfileViewModelFactory {
            if (INSTANCE == null) {
                synchronized(OtherProfileViewModelFactory::class.java) {
                    INSTANCE = OtherProfileViewModelFactory(sessionRepository, userRepository, username)
                }
            }
            return INSTANCE as OtherProfileViewModelFactory
        }
    }
}