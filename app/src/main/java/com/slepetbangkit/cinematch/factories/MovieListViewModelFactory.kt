package com.slepetbangkit.cinematch.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.slepetbangkit.cinematch.data.repository.MovieListRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.view.profile.movielist.MovieListViewModel
import com.slepetbangkit.cinematch.view.profile.movielist.create.CreateMovieListViewModel

class MovieListViewModelFactory private constructor(
    private val sessionRepository: SessionRepository,
    private val movieListRepository: MovieListRepository
) : ViewModelProvider.NewInstanceFactory() {

    private var listId: String = ""

    fun updateListId(listId: String) {
        this.listId = listId
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MovieListViewModel::class.java) -> {
                MovieListViewModel(sessionRepository, movieListRepository, listId) as T
            }
            modelClass.isAssignableFrom(CreateMovieListViewModel::class.java) -> {
                CreateMovieListViewModel(movieListRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: MovieListViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            sessionRepository: SessionRepository,
            movieListRepository: MovieListRepository
        ): MovieListViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MovieListViewModelFactory(sessionRepository, movieListRepository).also {
                    INSTANCE = it
                }
            }
        }
    }
}
