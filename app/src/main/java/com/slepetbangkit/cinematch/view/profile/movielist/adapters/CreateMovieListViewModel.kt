package com.slepetbangkit.cinematch.view.profile.movielist.adapters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slepetbangkit.cinematch.data.remote.response.PlaylistsItem
import com.slepetbangkit.cinematch.data.repository.MovieListRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

class CreateMovieListViewModel(private val movieListRepository: MovieListRepository) : ViewModel() {
    private val _createMovieListResult = MutableLiveData<PlaylistsItem>()
    val createMovieListResult: LiveData<PlaylistsItem> = _createMovieListResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun createNewList(
        name: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val response = movieListRepository.createNewMovieList(name)
                _createMovieListResult.value = response
            } catch(e: SocketTimeoutException) {
                _error.value = "Create Movie List Failed: ${e.message}"
            } catch (e: HttpException) {
                _error.value = "Create Movie List Failed: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}