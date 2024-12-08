package com.afrinacapstone.ecocraft.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    private val _profileName = MutableLiveData<String>()
    val profileName: LiveData<String> get() = _profileName

    private val _profilePicture = MutableLiveData<String>()
    val profilePicture: LiveData<String> get() = _profilePicture

    fun updateProfileName(name: String) {
        _profileName.value = name
    }

    fun updateProfilePicture(picturePath: String) {
        _profilePicture.value = picturePath
    }
}
