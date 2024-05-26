package com.slepetbangkit.cinematch.data.local.repository

import com.slepetbangkit.cinematch.data.local.preferences.SessionPreferences
import com.slepetbangkit.cinematch.data.remote.response.LoginResponse
import com.slepetbangkit.cinematch.data.remote.response.ProfileResponse
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiService
import com.slepetbangkit.cinematch.data.remote.response.RegisterResponse
import kotlinx.coroutines.flow.first

class UserRepository(
    private val apiService: ApiService,
    private val prefs: SessionPreferences
) {
//    suspend fun registerUser(
//        username: String,
//        email: String,
//        password: String,
//        password2: String,
//        bio: String
//    ): RegisterResponse {
//        return apiService.register(username, email, password, password2, bio)
//    }
//
//    suspend fun loginUser(
//        username: String,
//        password: String
//    ): LoginResponse {
//        return apiService.login(username, password)
//    }
//
//    suspend fun getProfile(): ProfileResponse {
//        val token = prefs.getAccessToken().first()
//        return apiService.getProfile("Bearer $token")
//    }
}
