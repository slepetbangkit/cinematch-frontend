package com.slepetbangkit.cinematch.data.repository

import android.util.Log
import com.slepetbangkit.cinematch.data.remote.request.UpdatePlaylistRequest
import com.slepetbangkit.cinematch.data.remote.response.PlaylistsItem
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiService

class MovieListRepository (
    private val sessionRepository: SessionRepository,
    private val apiService: ApiService
) {
    suspend fun getMovieListDetails(listId: String): PlaylistsItem {
        val accessToken = sessionRepository.getAccessToken()
        return apiService.getMovieListDetails("Bearer $accessToken", listId)
    }

    suspend fun createNewMovieList(name: String): PlaylistsItem {
        val accessToken = sessionRepository.getAccessToken()
        return apiService.createMovieList("Bearer $accessToken", name)
    }

    suspend fun deleteMovieById(listId: String, tmdbId: Int): PlaylistsItem {
        val accessToken = sessionRepository.getAccessToken()
        val request = UpdatePlaylistRequest(delete_movie_tmdb_id = listOf(tmdbId))
        return apiService.updatePlaylist(
            "Bearer $accessToken",
            listId,
            request
        )
    }


    companion object {
        @Volatile
        private var INSTANCE: MovieListRepository? = null

        fun getInstance(
            sessionRepository: SessionRepository,
            apiService: ApiService
        ): MovieListRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = MovieListRepository(sessionRepository, apiService)
                INSTANCE = instance
                instance
            }
        }
    }
}