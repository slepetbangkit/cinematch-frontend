package com.slepetbangkit.cinematch.view.moviedetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.MovieDetailsResponse
import com.slepetbangkit.cinematch.data.repository.MovieRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MovieDetailsViewModel(
    private val sessionRepository: SessionRepository,
    private val movieRepository: MovieRepository,
    private val selectedMovie: Int
) : ViewModel() {
    private val _tmdbId = MutableLiveData<Int>()
    val tmdbId: LiveData<Int> = _tmdbId

    private val _movieDetail = MutableLiveData<MovieDetailsResponse>()
    val movieDetail: LiveData<MovieDetailsResponse> get() = _movieDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        viewModelScope.launch {
            getMovieDetails()
        }
    }

    private suspend fun getMovieDetails() {
        try {
            _isLoading.value = true
            val response = movieRepository.getMovieDetails(selectedMovie)
            _movieDetail.value = response
        } catch (e: HttpException) {
            if (e.code() == 401) {
                sessionRepository.refresh()
                getMovieDetails()
            } else {
                _error.value = "Error fetching data"
            }
        } finally {
            _isLoading.value = false
        }
    }
}