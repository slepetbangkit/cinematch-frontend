package com.slepetbangkit.cinematch.view.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedProfileViewModel: ViewModel() {
    private val _isProfileUpdated: MutableLiveData<Boolean> = MutableLiveData(false)
    val isProfileUpdated get() = _isProfileUpdated

    fun setProfileUpdated(isUpdated: Boolean) {
        _isProfileUpdated.value = isUpdated
    }
}