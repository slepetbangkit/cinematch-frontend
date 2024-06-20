package com.slepetbangkit.cinematch.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.slepetbangkit.cinematch.data.repository.MovieRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.view.search.viewmodels.SearchMovieViewModel

class SearchMovieViewModelFactory private constructor(
    private val sessionRepository: SessionRepository,
    private val movieRepository: MovieRepository,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SearchMovieViewModel::class.java) -> {
                SearchMovieViewModel(sessionRepository, movieRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SearchMovieViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            sessionRepository: SessionRepository,
            movieRepository: MovieRepository,
        ): SearchMovieViewModelFactory {
            if (INSTANCE == null) {
                synchronized(SearchMovieViewModelFactory::class.java) {
                    INSTANCE = SearchMovieViewModelFactory(sessionRepository, movieRepository)
                }
            }
            return INSTANCE as SearchMovieViewModelFactory
        }
    }
}