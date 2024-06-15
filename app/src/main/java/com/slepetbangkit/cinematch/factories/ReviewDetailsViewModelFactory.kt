package com.slepetbangkit.cinematch.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.slepetbangkit.cinematch.data.repository.MovieRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.view.review.detail.ReviewDetailViewModel

class ReviewDetailsViewModelFactory private constructor(
    private val sessionRepository: SessionRepository,
    private val movieRepository: MovieRepository,
    private val reviewId: String = ""
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ReviewDetailViewModel::class.java) -> {
                ReviewDetailViewModel(sessionRepository, movieRepository, reviewId) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ReviewDetailsViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            sessionRepository: SessionRepository,
            movieRepository: MovieRepository,
            reviewId: String
        ): ReviewDetailsViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ReviewDetailsViewModelFactory::class.java) {
                    INSTANCE = ReviewDetailsViewModelFactory(sessionRepository, movieRepository, reviewId)
                }
            }
            return INSTANCE as ReviewDetailsViewModelFactory
        }
    }
}