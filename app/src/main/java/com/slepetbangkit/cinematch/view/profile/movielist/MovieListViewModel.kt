package com.slepetbangkit.cinematch.view.profile.movielist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.MoviesItem
import com.slepetbangkit.cinematch.data.remote.response.PlaylistsItem
import com.slepetbangkit.cinematch.data.remote.response.ProfileResponse
import com.slepetbangkit.cinematch.data.repository.MovieListRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MovieListViewModel(
    private val sessionRepository: SessionRepository,
    private val movieListRepository: MovieListRepository,
    listId: String
): ViewModel() {
    private val _movieListDetails = MutableLiveData<PlaylistsItem>()
    val movieListDetails: LiveData<PlaylistsItem> get() = _movieListDetails

    private val _movies = MutableLiveData<List<MoviesItem>>()
    val movies: LiveData<List<MoviesItem>> get() = _movies

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        fetchMovieListDetails(listId)
    }

    fun fetchMovieListDetails(listId: String) {
        viewModelScope.launch {
            getMovieListDetails(listId)
        }
    }

    private suspend fun getMovieListDetails(listId: String) {
        try {
            _isLoading.value = true
            val listResponse = movieListRepository.getMovieListDetails(listId)
            _movieListDetails.value = listResponse
            _movies.value = listResponse.movies
        } catch (e: HttpException) {
            if (e.code() == 401) {
                sessionRepository.refresh()
                getMovieListDetails(listId)
            } else {
                _error.value = e.message()
            }
        } finally {
            _isLoading.value = false
        }
    }

    fun deleteMovieById(listId: String, tmdbId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val listResponse = movieListRepository.deleteMovieById(listId, tmdbId)
                _movieListDetails.value = listResponse
                _movies.value = listResponse.movies
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    sessionRepository.refresh()
                    getMovieListDetails(listId)
                } else {
                    _error.value = e.message()
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}