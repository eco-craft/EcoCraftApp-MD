package com.afrinacapstone.ecocraft.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afrinacapstone.ecocraft.api.response.GetUserResponse
import com.afrinacapstone.ecocraft.preferences.UserPreference
import com.afrinacapstone.ecocraft.repository.ProfileRepository
import com.afrinacapstone.ecocraft.util.Resource
import com.afrinacapstone.ecocraft.util.parseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userPreference: UserPreference
) :
    ViewModel() {

    private val _userUiState = MutableLiveData<Resource<GetUserResponse>>()
    val userUiState: LiveData<Resource<GetUserResponse>> = _userUiState

    fun getUser() {
        viewModelScope.launch {
            _userUiState.value = Resource.Loading()

            try {
                val response = profileRepository.getUser()
                if (response.isSuccessful) {
                    response.body()?.let {
                        val user = parseResponse<GetUserResponse>(it)
                        _userUiState.value = Resource.Success(user)
                    }
                } else {
                    response.errorBody()?.let {
                        _userUiState.value = Resource.Error(it.toString())
                    }
                }
            } catch (e: Exception) {
                _userUiState.value = Resource.Error(e.message.toString())
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreference.saveLoginStatus(false)
            userPreference.saveAccessToken("")
        }
    }
}
