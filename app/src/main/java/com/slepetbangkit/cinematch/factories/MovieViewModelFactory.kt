package com.slepetbangkit.cinematch.factories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.slepetbangkit.cinematch.data.repository.MovieRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.view.moviedetails.MovieDetailsViewModel
import com.slepetbangkit.cinematch.view.review.ReviewViewModel
import com.slepetbangkit.cinematch.view.review.add.AddReviewViewModel

class MovieViewModelFactory private constructor(
    private val sessionRepository: SessionRepository,
    private val movieRepository: MovieRepository
) : ViewModelProvider.NewInstanceFactory() {

    private var tmdbId: Int = 0

    fun updateTmdbId(tmdbId: Int) {
        this.tmdbId = tmdbId
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MovieDetailsViewModel::class.java) -> {
                MovieDetailsViewModel(sessionRepository, movieRepository, tmdbId) as T
            }
            modelClass.isAssignableFrom(ReviewViewModel::class.java) -> {
                ReviewViewModel(sessionRepository, movieRepository, tmdbId) as T
            }
            modelClass.isAssignableFrom(AddReviewViewModel::class.java) -> {
                AddReviewViewModel(sessionRepository, movieRepository, tmdbId) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: MovieViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            sessionRepository: SessionRepository,
            movieRepository: MovieRepository
        ): MovieViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MovieViewModelFactory(sessionRepository, movieRepository).also {
                    INSTANCE = it
                }
            }
        }
    }
}
