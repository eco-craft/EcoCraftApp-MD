package com.afrinacapstone.ecocraft.ui.home.add_post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afrinacapstone.ecocraft.api.response.PostFormErrorResponse
import com.afrinacapstone.ecocraft.repository.CraftsRepository
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
class PostFormViewModel @Inject constructor(private val craftRepository: CraftsRepository) :
    ViewModel() {

    private val _postCraftUiState = MutableLiveData<Resource<String>>()
    val postCraftUiState: LiveData<Resource<String>> = _postCraftUiState

    private val _editCraftUiState = MutableLiveData<Resource<String>>()
    val editCraftUiState: LiveData<Resource<String>> = _editCraftUiState

    private var _imagePath: String? = null
    val imagePath: String? get() = _imagePath

    fun setImagePath(path: String) {
        _imagePath = path
    }

    fun postCraft(
        title: String,
        materials: String,
        description: String,
        craftImage: File
    ) {
        viewModelScope.launch {
            _postCraftUiState.value = Resource.Loading()

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .apply {
                    addFormDataPart("title", title)
                    craftImage.let {
                        val requestImageFile = it.asRequestBody("image/jpeg".toMediaType())
                        val profileImage = MultipartBody.Part.createFormData(
                            "craftImage",
                            it.name,
                            requestImageFile
                        )
                        addPart(profileImage)
                    }
                }

            materials.split(",").forEach {
                requestBody.addFormDataPart("materials[]", it)
            }
            description.split(",").forEach {
                requestBody.addFormDataPart("steps[]", it)
            }

            try {
                val response =
                    craftRepository.postCraft(requestBody.build())

                if (response.isSuccessful) {
                    _postCraftUiState.value = Resource.Success("Craft idea created successfully")
                } else {
                    response.errorBody()?.let { errorBody ->
                        val error = parseResponse<PostFormErrorResponse>(errorBody)
                        _postCraftUiState.value = Resource.Error(error.error.message)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _postCraftUiState.value = Resource.Error(e.message.toString())
            }
        }
    }

    fun updateCraft(
        id: String,
        title: String?,
        materials: String?,
        description: String?,
        image: File?
    ) {
        viewModelScope.launch {
            _editCraftUiState.value = Resource.Loading()
            try {
                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .apply {
                        title?.let { addFormDataPart("title", it) }
                        image?.let {
                            val requestImageFile = it.asRequestBody("image/jpeg".toMediaType())
                            val profileImage = MultipartBody.Part.createFormData(
                                "craftImage",
                                it.name,
                                requestImageFile
                            )
                            addPart(profileImage)
                        }
                    }

                materials!!.split(",").forEach {
                    requestBody.addFormDataPart("materials[]", it)
                }
                description!!.split(",").forEach {
                    requestBody.addFormDataPart("steps[]", it)
                }

                val response = craftRepository.updateUserProfile(
                    id,
                    requestBody.build()
                )

                when {
                    response.isSuccessful -> {
                        _editCraftUiState.value = Resource.Success("Craft updated successfully")
                    }
                    response.code() == 403 -> {
                        _editCraftUiState.value = Resource.Error("You are not authorized to update this craft")
                    }
                    else -> {
                        response.errorBody()?.let { errorBody ->
                            val error = parseResponse<PostFormErrorResponse>(errorBody)
                            _editCraftUiState.value = Resource.Error(error.error.message)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _editCraftUiState.value = Resource.Error(e.message.toString())
            }
        }
    }
}