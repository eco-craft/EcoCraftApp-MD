package com.afrinacapstone.ecocraft.ui.profile.edit_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afrinacapstone.ecocraft.api.response.GetUserResponse
import com.afrinacapstone.ecocraft.repository.ProfileRepository
import com.afrinacapstone.ecocraft.util.Resource
import com.afrinacapstone.ecocraft.util.parseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) :
    ViewModel() {

    private val _userUiState = MutableLiveData<Resource<GetUserResponse>>()
    val userUiState: LiveData<Resource<GetUserResponse>> = _userUiState

    private val _updateProfileUiState = MutableLiveData<Resource<String>>()
    val updateProfileUiState: LiveData<Resource<String>> = _updateProfileUiState

    private var _imagePath: String? = null
    val imagePath: String? get() = _imagePath

    private var _user: GetUserResponse? = null
    val user get() = _user!!

    init {
        getUser()
    }

    private fun setUser(user: GetUserResponse) {
        _user = user
    }

    fun setImagePath(path: String) {
        _imagePath = path
    }

    private fun getUser() {
        viewModelScope.launch {
            _userUiState.value = Resource.Loading()

            try {
                val response = profileRepository.getUser()
                if (response.isSuccessful) {
                    response.body()?.let {
                        val user = parseResponse<GetUserResponse>(it)
                        setUser(user)
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

    fun updateUserProfile(
        name: String?,
        phone: String?,
        email: String?,
        address: String?,
        photo: File?
    ) {
        viewModelScope.launch {
            _updateProfileUiState.value = Resource.Loading()
            try {
                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .apply {
                        name?.let { addFormDataPart("name", it) }
                        phone?.let { addFormDataPart("phone", it) }
                        email?.let { addFormDataPart("email", it) }
                        address?.let { addFormDataPart("address", it) }

                        photo?.let {
                            val requestImageFile = it.asRequestBody("image/jpeg".toMediaType())
                            val profileImage = MultipartBody.Part.createFormData(
                                "profilePicture",
                                it.name,
                                requestImageFile
                            )
                            addPart(profileImage)
                        }
                    }
                    .build()

                val response = profileRepository.updateUserProfile(
                    requestBody
                )

                if (response.isSuccessful) {
                    _updateProfileUiState.value = Resource.Success("Profile updated successfully")
                } else {
                    _updateProfileUiState.value = Resource.Error("Failed to update profile")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _updateProfileUiState.value = Resource.Error(e.toString())
            }
        }
    }
}