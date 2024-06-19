package com.slepetbangkit.cinematch.data.repository

import com.slepetbangkit.cinematch.data.remote.response.HomeResponse
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiService

class HomeRepository(
    private val sessionRepository: SessionRepository,
    private val apiService: ApiService
) {
    suspend fun getHomeMovies(): HomeResponse {
        val accessToken = sessionRepository.getAccessToken()
        return apiService.getHomeMovies("Bearer $accessToken")
    }

    companion object {
        @Volatile
        private var INSTANCE: HomeRepository? = null

        fun getInstance(
            sessionRepository: SessionRepository,
            apiService: ApiService
        ): HomeRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = HomeRepository(sessionRepository, apiService)
                INSTANCE = instance
                instance
            }
        }
    }
}