package com.slepetbangkit.cinematch.view.moviedetails.addtolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.PlaylistsItem
import com.slepetbangkit.cinematch.data.repository.MovieListRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException

class AddMovieToListViewModel(
    private val sessionRepository: SessionRepository,
    private val movieListRepository: MovieListRepository,
) : ViewModel() {
    private val _movieListDetails = MutableLiveData<List<PlaylistsItem>>()
    val movieListDetails: LiveData<List<PlaylistsItem>> get() = _movieListDetails

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        viewModelScope.launch {
            getListUser()
        }
    }

    fun fetchUserLists() {
        viewModelScope.launch {
            getListUser()
        }
    }


    private suspend fun getListUser() {
        try {
            _isLoading.value = true
            val response = movieListRepository.getMovieList()
            _movieListDetails.value = response
        } catch (e: SocketTimeoutException) {
            _error.value = "Error fetching data"
            getListUser()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                withContext(Dispatchers.IO) {
                    sessionRepository.refresh()
                }
                getListUser()
            } else {
                _error.value = "Error fetching data"
            }
        } finally {
            _isLoading.value = false
        }
    }

    fun addMovieToList(listId: String, movieId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                movieListRepository.addMovieById(listId, movieId)
                getListUser()
            } catch (e: SocketTimeoutException) {
                _error.value = "Add Movie to List Failed: ${e.message}"
            } catch (e: HttpException) {
                _error.value = "Add Movie to List Failed: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteMovieFromList(listId: String, movieId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                movieListRepository.deleteMovieById(listId, movieId)
                getListUser()
            } catch (e: SocketTimeoutException) {
                _error.value = "Delete Movie from List Failed: ${e.message}"
            } catch (e: HttpException) {
                _error.value = "Delete Movie from List Failed: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
