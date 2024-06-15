package com.slepetbangkit.cinematch.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.slepetbangkit.cinematch.data.repository.MovieRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.view.moviedetails.MovieDetailsViewModel
import com.slepetbangkit.cinematch.view.review.ReviewViewModel
import com.slepetbangkit.cinematch.view.review.add.AddReviewViewModel

class MovieViewModelFactory private constructor(
    private val sessionRepository: SessionRepository,
    private val movieRepository: MovieRepository,
    private val tmdbId: Int = 0
) : ViewModelProvider.NewInstanceFactory() {

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
            movieRepository: MovieRepository,
            tmdbId: Int
        ): MovieViewModelFactory {
            if (INSTANCE == null) {
                synchronized(MovieViewModelFactory::class.java) {
                    INSTANCE = MovieViewModelFactory(sessionRepository, movieRepository, tmdbId)
                }
            }
            return INSTANCE as MovieViewModelFactory
        }
    }
}