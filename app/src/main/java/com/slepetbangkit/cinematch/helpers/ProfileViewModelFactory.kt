package com.slepetbangkit.cinematch.helpers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.view.profile.ProfileViewModel
import com.slepetbangkit.cinematch.view.profile.followlist.FollowListViewModel

class ProfileViewModelFactory private constructor(private val sessionRepository: SessionRepository, private val username: String = "") :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(FollowListViewModel::class.java) -> {
                FollowListViewModel(sessionRepository, username) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(sessionRepository, username) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @JvmStatic
        fun getInstance(sessionRepository: SessionRepository, username: String): ProfileViewModelFactory {
            return ProfileViewModelFactory(sessionRepository, username)
        }
    }
}