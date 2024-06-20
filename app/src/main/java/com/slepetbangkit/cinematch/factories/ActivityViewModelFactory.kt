package com.slepetbangkit.cinematch.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.slepetbangkit.cinematch.data.repository.ActivityRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.view.activity.ActivityViewModel

class ActivityViewModelFactory private constructor(
    private val sessionRepository: SessionRepository,
    private val activityRepository: ActivityRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ActivityViewModel::class.java) -> {
                ActivityViewModel(sessionRepository, activityRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ActivityViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            sessionRepository: SessionRepository,
            activityRepository: ActivityRepository
        ): ActivityViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ActivityViewModel::class.java) {
                    INSTANCE = ActivityViewModelFactory(sessionRepository, activityRepository)
                }
            }
            return INSTANCE as ActivityViewModelFactory
        }
    }
}