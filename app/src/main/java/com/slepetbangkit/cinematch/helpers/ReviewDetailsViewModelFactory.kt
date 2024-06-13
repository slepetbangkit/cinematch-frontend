package com.slepetbangkit.cinematch.helpers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.view.profile.ProfileViewModel
import com.slepetbangkit.cinematch.view.review.detail.ReviewDetailViewModel

class ReviewDetailsViewModelFactory private constructor(private val sessionRepository: SessionRepository, private val reviewId: String = "") :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ReviewDetailViewModel::class.java) -> {
                ReviewDetailViewModel(sessionRepository, reviewId) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @JvmStatic
        fun getInstance(sessionRepository: SessionRepository, reviewId: String): ReviewDetailsViewModelFactory {
            return ReviewDetailsViewModelFactory(sessionRepository, reviewId)
        }
    }
}