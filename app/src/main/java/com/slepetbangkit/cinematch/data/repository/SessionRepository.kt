package com.slepetbangkit.cinematch.data.repository

import com.slepetbangkit.cinematch.data.preferences.SessionPreferences
import com.slepetbangkit.cinematch.data.remote.response.LoginResponse
import com.slepetbangkit.cinematch.data.remote.response.RegisterResponse
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import retrofit2.HttpException

class SessionRepository(
    private val sessionPreferences: SessionPreferences,
    private val apiService: ApiService
) {
    fun getAccessTokenFlow(): Flow<String> {
        return sessionPreferences.getAccessToken()
    }

    suspend fun getAccessToken(): String {
        return sessionPreferences.getAccessToken().first()
    }

    suspend fun saveAccessToken(accessToken: String) {
        sessionPreferences.saveAccessToken(accessToken)
    }

    private suspend fun getRefreshToken(): String {
        return sessionPreferences.getRefreshToken().first()
    }

    suspend fun saveRefreshToken(refreshToken: String) {
        sessionPreferences.saveRefreshToken(refreshToken)
    }

    suspend fun getUsername(): String {
        return sessionPreferences.getUsername().first()
    }

    suspend fun saveUsername(accessToken: String) {
        sessionPreferences.saveUsername(accessToken)
    }

    suspend fun register(username: String, email: String, password: String): RegisterResponse {
        return apiService.register(username, email, password)
    }

    suspend fun login(username: String, password: String): LoginResponse {
        return apiService.login(username, password)
    }

    suspend fun refresh() {
        try {
            val refreshToken = getRefreshToken()
            val response = apiService.refresh(refreshToken)
            response.let {
                saveAccessToken(it.access)
                saveRefreshToken(it.refresh)
            }
        } catch (e: HttpException) {
            logout()
        }
    }

    suspend fun logout() {
        sessionPreferences.clear()
    }

    companion object {
        @Volatile
        private var INSTANCE: SessionRepository? = null

        fun getInstance(
            sessionPreferences: SessionPreferences,
            apiService: ApiService
        ): SessionRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionRepository(sessionPreferences, apiService)
                INSTANCE = instance
                instance
            }
        }
    }
}