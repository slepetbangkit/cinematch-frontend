package com.slepetbangkit.cinematch.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.slepetbangkit.cinematch.data.local.preferences.SessionPreferences
import kotlinx.coroutines.flow.Flow

class SessionRepository(dataStore: DataStore<Preferences>) {
    private val sessionPreferences = SessionPreferences.getInstance(dataStore)

    fun getAccessToken(): Flow<String> {
        return sessionPreferences.getAccessToken()
    }

    suspend fun saveAccessToken(accessToken: String) {
        sessionPreferences.saveAccessToken(accessToken)
    }

    fun getRefreshToken(): Flow<String> {
        return sessionPreferences.getRefreshToken()
    }

    suspend fun saveRefreshToken(refreshToken: String) {
        sessionPreferences.saveRefreshToken(refreshToken)
    }

    fun getUsername(): Flow<String> {
        return sessionPreferences.getUsername()
    }

    suspend fun saveUsername(accessToken: String) {
        sessionPreferences.saveUsername(accessToken)
    }

    suspend fun clear() {
        sessionPreferences.clear()
    }

    companion object {
        @Volatile
        private var INSTANCE: SessionRepository? = null

        fun getInstance(dataStore: DataStore<Preferences>): SessionRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionRepository(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}