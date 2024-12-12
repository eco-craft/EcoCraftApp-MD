package com.afrinacapstone.ecocraft.ui.profile.edit_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afrinacapstone.ecocraft.api.response.UpdatePasswordErrorResponse
import com.afrinacapstone.ecocraft.repository.ProfileRepository
import com.afrinacapstone.ecocraft.util.Resource
import com.afrinacapstone.ecocraft.util.parseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class EditPasswordViewModel @Inject constructor(private val profileRepository: ProfileRepository) :
    ViewModel() {

    private var _updatePasswordState = MutableLiveData<Resource<String>>()
    val updatePasswordState: LiveData<Resource<String>> = _updatePasswordState

    fun updatePassword(currentPassword: String, newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            _updatePasswordState.value = Resource.Loading()

            try {
                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("currentPassword", currentPassword)
                    .addFormDataPart("password", newPassword)
                    .addFormDataPart("passwordConfirmation", confirmPassword)
                    .build()
                val response = profileRepository.updatePassword(requestBody)

                if (response.isSuccessful) {
                    _updatePasswordState.value = Resource.Success("Password updated successfully")
                } else {
                    response.errorBody()?.let {
                        val error = parseResponse<UpdatePasswordErrorResponse>(it)
                        _updatePasswordState.value = Resource.Error(error.error.message)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _updatePasswordState.value = Resource.Error("Failed to update password")
            }
        }
    }
}