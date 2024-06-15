package com.slepetbangkit.cinematch.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.slepetbangkit.cinematch.data.repository.SessionRepository

class MainViewModel(sessionRepository: SessionRepository): ViewModel() {
    val accessToken: LiveData<String> = sessionRepository.getAccessTokenFlow().asLiveData()
}