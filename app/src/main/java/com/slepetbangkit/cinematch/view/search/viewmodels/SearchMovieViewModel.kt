package com.slepetbangkit.cinematch.view.search.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slepetbangkit.cinematch.data.remote.response.MovieSearchResponseItem
import com.slepetbangkit.cinematch.data.repository.MovieRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import retrofit2.HttpException

class SearchMovieViewModel(
    private val sessionRepository: SessionRepository,
    private val movieRepository: MovieRepository
) : ViewModel() {
    private val _searchMovieResult = MutableLiveData<List<MovieSearchResponseItem>>()
    val searchMovieResult: LiveData<List<MovieSearchResponseItem>> = _searchMovieResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    suspend fun searchMovies(movieName: String) {
        try {
            _isLoading.value = true
            val response = movieRepository.searchMovies(movieName)
            _searchMovieResult.value = response
        } catch (e: HttpException) {
            if (e.code() == 401) {
                sessionRepository.refresh()
                searchMovies(movieName)
            } else {
                _error.value = e.message()
            }
        } finally {
            _isLoading.value = false
        }
    }
}
