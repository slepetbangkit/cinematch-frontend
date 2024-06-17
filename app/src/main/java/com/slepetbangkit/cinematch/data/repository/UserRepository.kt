package com.slepetbangkit.cinematch.data.repository

import com.slepetbangkit.cinematch.data.remote.response.FollowListResponse
import com.slepetbangkit.cinematch.data.remote.response.MessageResponse
import com.slepetbangkit.cinematch.data.remote.response.ProfileResponse
import com.slepetbangkit.cinematch.data.remote.response.UserSearchResponse
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import java.io.File

class UserRepository(
    private val sessionRepository: SessionRepository,
    private val apiService: ApiService
) {
    suspend fun searchUser(query: String): UserSearchResponse {
        val accessToken = sessionRepository.getAccessToken()
        return apiService.searchUser("Bearer $accessToken", query)
    }

    suspend fun getSelfProfile(): ProfileResponse {
        val accessToken = sessionRepository.getAccessToken()
        val username = sessionRepository.getUsername()
        return apiService.getProfile("Bearer $accessToken", username)
    }

    suspend fun updateSelfProfile(
        newUsername: RequestBody?,
        newBio: RequestBody?,
        profilePicture: MultipartBody.Part?
    ): MessageResponse {
        val accessToken = sessionRepository.getAccessToken()
        val username = sessionRepository.getUsername()
        return apiService.updateSelfProfile("Bearer $accessToken", username, newUsername, newBio, profilePicture)
    }

    suspend fun getSelfFollowList(): FollowListResponse {
        val accessToken = sessionRepository.getAccessToken()
        val username = sessionRepository.getUsername()
        return apiService.getFollowList("Bearer $accessToken", username)
    }

    suspend fun getOtherProfile(username: String): ProfileResponse {
        val accessToken = sessionRepository.getAccessToken()
        return apiService.getProfile("Bearer $accessToken", username)
    }

    suspend fun getOtherFollowList(username: String): FollowListResponse {
        val accessToken = sessionRepository.getAccessToken()
        return apiService.getFollowList("Bearer $accessToken", username)
    }

    suspend fun follow(username: String): MessageResponse {
        val accessToken = sessionRepository.getAccessToken()
        return apiService.followUser("Bearer $accessToken", username)
    }

    suspend fun unfollow(username: String): MessageResponse {
        val accessToken = sessionRepository.getAccessToken()
        return apiService.unfollowUser("Bearer $accessToken", username)
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(
            sessionRepository: SessionRepository,
            apiService: ApiService
        ): UserRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = UserRepository(sessionRepository, apiService)
                INSTANCE = instance
                instance
            }
        }
    }
}