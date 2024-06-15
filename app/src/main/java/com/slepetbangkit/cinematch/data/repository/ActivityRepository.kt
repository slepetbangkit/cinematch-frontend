package com.slepetbangkit.cinematch.data.repository

import com.slepetbangkit.cinematch.data.remote.response.ActivityResponse
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiService

class ActivityRepository(
    private val sessionRepository: SessionRepository,
    private val apiService: ApiService
) {
    suspend fun getActivities(): ActivityResponse {
        val accessToken = sessionRepository.getAccessToken()
        return apiService.getActivities("Bearer $accessToken")
    }

    companion object {
        @Volatile
        private var INSTANCE: ActivityRepository? = null

        fun getInstance(
            sessionRepository: SessionRepository,
            apiService: ApiService
        ): ActivityRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = ActivityRepository(sessionRepository, apiService)
                INSTANCE = instance
                instance
            }
        }
    }
}