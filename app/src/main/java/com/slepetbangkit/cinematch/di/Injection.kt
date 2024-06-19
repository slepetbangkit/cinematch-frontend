package com.slepetbangkit.cinematch.di

import android.content.Context
import com.slepetbangkit.cinematch.data.preferences.SessionPreferences
import com.slepetbangkit.cinematch.data.preferences.dataStore
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiConfig
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiService
import com.slepetbangkit.cinematch.data.repository.ActivityRepository
import com.slepetbangkit.cinematch.data.repository.HomeRepository
import com.slepetbangkit.cinematch.data.repository.MovieListRepository
import com.slepetbangkit.cinematch.data.repository.MovieRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository

object Injection {
    private fun provideApiService(): ApiService {
        return ApiConfig.getApiService()
    }

    fun provideSessionRepository(context: Context): SessionRepository {
        val sessionPreferences = SessionPreferences.getInstance(context.dataStore)
        val apiService = provideApiService()
        return SessionRepository.getInstance(sessionPreferences, apiService)
    }

    fun provideHomeRepository(context: Context): HomeRepository {
        val sessionRepository = provideSessionRepository(context)
        val apiService = provideApiService()
        return HomeRepository.getInstance(sessionRepository, apiService)
    }

    fun provideActivityRepository(context: Context): ActivityRepository {
        val sessionRepository = provideSessionRepository(context)
        val apiService = provideApiService()
        return ActivityRepository.getInstance(sessionRepository, apiService)
    }

    fun provideMovieRepository(context: Context): MovieRepository {
        val sessionRepository = provideSessionRepository(context)
        val apiService = provideApiService()
        return MovieRepository.getInstance(sessionRepository, apiService)
    }

    fun provideUserRepository(context: Context): UserRepository {
        val sessionRepository = provideSessionRepository(context)
        val apiService = provideApiService()
        return UserRepository.getInstance(sessionRepository, apiService)
    }

    fun provideMovieListRepository(context: Context): MovieListRepository {
        val sessionRepository = provideSessionRepository(context)
        val apiService = provideApiService()
        return MovieListRepository.getInstance(sessionRepository, apiService)
    }
}