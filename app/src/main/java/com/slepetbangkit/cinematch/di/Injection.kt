package com.slepetbangkit.cinematch.di

import android.content.Context
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiConfig
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiService

object Injection {
    private fun provideApiService(): ApiService {
        return ApiConfig.getApiService()
    }

//    fun provideUserRepository(context: Context): UserRepository {
//        val pref = UserPreference.getInstance(context.dataStore)
//        val apiService = provideApiService()
//        return UserRepository.getInstance(pref, apiService)
//    }
//
//    fun provideStoriesRepository(): StoriesRepository {
//        val apiService = provideApiService()
//        return StoriesRepository.getInstance(apiService)
//    }
}