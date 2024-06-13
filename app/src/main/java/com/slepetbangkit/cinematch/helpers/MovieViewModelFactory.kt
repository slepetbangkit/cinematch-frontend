package com.slepetbangkit.cinematch.helpers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.view.moviedetails.MovieViewModel
import com.slepetbangkit.cinematch.view.profile.ProfileViewModel
import com.slepetbangkit.cinematch.view.review.MovieReviewsViewModel

class MovieViewModelFactory private constructor(private val sessionRepository: SessionRepository, private val tmdbId: Int = 0) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MovieViewModel::class.java) -> {
                MovieViewModel(sessionRepository, tmdbId) as T
            }
            modelClass.isAssignableFrom(MovieReviewsViewModel::class.java) -> {
                MovieReviewsViewModel(sessionRepository, tmdbId) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @JvmStatic
        fun getInstance(sessionRepository: SessionRepository, tmdbId: Int): MovieViewModelFactory {
            return MovieViewModelFactory(sessionRepository, tmdbId)
        }
    }
}