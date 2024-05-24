package com.slepetbangkit.cinematch.data

import com.slepetbangkit.cinematch.api.ApiService
import com.slepetbangkit.cinematch.api.response.RegisterResponse

class UserRepository(private val apiService: ApiService) {
    suspend fun registerUser(
        username: String,
        email: String,
        password: String,
        password2: String,
        bio: String
    ): RegisterResponse {
        return apiService.register(username, email, password, password2, bio)
    }
}
