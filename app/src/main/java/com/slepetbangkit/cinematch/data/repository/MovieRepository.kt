package com.slepetbangkit.cinematch.data.repository

import com.slepetbangkit.cinematch.data.remote.response.AddReviewResponse
import com.slepetbangkit.cinematch.data.remote.response.MovieDetailsResponse
import com.slepetbangkit.cinematch.data.remote.response.MovieReviewsResponse
import com.slepetbangkit.cinematch.data.remote.response.MovieSearchResponseItem
import com.slepetbangkit.cinematch.data.remote.response.ReviewDetailsResponse
import com.slepetbangkit.cinematch.data.remote.retrofit.ApiService

class MovieRepository(
    private val sessionRepository: SessionRepository,
    private val apiService: ApiService
) {
    suspend fun searchMovies(query: String): List<MovieSearchResponseItem> {
        val accessToken = sessionRepository.getAccessToken()
        return apiService.searchMovies("Bearer $accessToken", query)
    }

    suspend fun getMovieDetails(tmbdId: Int): MovieDetailsResponse {
        val accessToken = sessionRepository.getAccessToken()
        return apiService.getMovieDetails("Bearer $accessToken", tmbdId)
    }

    suspend fun getMovieReviews(tmbdId: Int): MovieReviewsResponse {
        val accessToken = sessionRepository.getAccessToken()
        return apiService.getMovieReviews("Bearer $accessToken", tmbdId)
    }

    suspend fun getReviewDetailsById(reviewId: String): ReviewDetailsResponse {
        val accessToken = sessionRepository.getAccessToken()
        return apiService.getReviewDetailsById("Bearer $accessToken", reviewId)
    }

    suspend fun addReview(tmbdId: Int, description: String): AddReviewResponse {
        val accessToken = sessionRepository.getAccessToken()
        return apiService.addReview("Bearer $accessToken", tmbdId, description)
    }

    companion object {
        @Volatile
        private var INSTANCE: MovieRepository? = null

        fun getInstance(
            sessionRepository: SessionRepository,
            apiService: ApiService
        ): MovieRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = MovieRepository(sessionRepository, apiService)
                INSTANCE = instance
                instance
            }
        }
    }
}