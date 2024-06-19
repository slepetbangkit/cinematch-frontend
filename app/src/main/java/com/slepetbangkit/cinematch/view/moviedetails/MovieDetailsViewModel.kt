package com.slepetbangkit.cinematch.view.moviedetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.MovieDetailsResponse
import com.slepetbangkit.cinematch.data.repository.MovieRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException

class MovieDetailsViewModel(
    private val sessionRepository: SessionRepository,
    private val movieRepository: MovieRepository,
    private val tmdbId: Int
) : ViewModel() {
    private var isFetched: Boolean = false

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

    fun fetchMovieDetails() {
        viewModelScope.launch {
            getMovieDetails()
        }
    }


    private suspend fun getMovieDetails() {
        try {
            if (!isFetched) {
                _isLoading.value = true
            }
            val response = movieRepository.getMovieDetails(tmdbId)
            _movieDetail.value = response
        } catch (e: SocketTimeoutException) {
            _error.value = "Error fetching data"
            getMovieDetails()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                withContext(Dispatchers.IO) {
                    sessionRepository.refresh()
                }
                getMovieDetails()
            } else {
                _error.value = "Error fetching data"
            }
        } finally {
            if (!isFetched) {
                _isLoading.value = false
                isFetched = true
            }
        }
    }
}
