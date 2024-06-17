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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException

class MovieListViewModel(
    private val sessionRepository: SessionRepository,
    private val movieListRepository: MovieListRepository,
    listId: String
): ViewModel() {
    private val _movieListDetails = MutableLiveData<PlaylistsItem>()
    val movieListDetails: LiveData<PlaylistsItem> get() = _movieListDetails

    private val _movies = MutableLiveData<List<MoviesItem>>()
    val movies: LiveData<List<MoviesItem>> get() = _movies

    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> = _isDeleted

    private val _isEdited = MutableLiveData<Boolean>()
    val isEdited: LiveData<Boolean> = _isEdited

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
        } catch(e: SocketTimeoutException) {
            _error.value = e.message
            getMovieListDetails(listId)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                withContext(Dispatchers.IO) {
                    sessionRepository.refresh()
                }
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

    fun deleteMovieListById(listId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                movieListRepository.deleteMovieListById(listId)
                _isDeleted.value = true
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

    fun editMovieList(listId: String, title: String, desc: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val listResponse = movieListRepository.editMovieList(listId, title, desc)
                _movieListDetails.value = listResponse
                _isEdited.value = true
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