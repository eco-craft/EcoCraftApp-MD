package com.afrinacapstone.ecocraft.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afrinacapstone.ecocraft.api.response.RegisterErrorResponse
import com.afrinacapstone.ecocraft.api.response.RegisterSuccessResponse
import com.afrinacapstone.ecocraft.repository.AuthRepository
import com.afrinacapstone.ecocraft.util.Resource
import com.afrinacapstone.ecocraft.util.parseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    private val _registerUiState = MutableLiveData<Resource<RegisterSuccessResponse>>()
    val registerUiState: LiveData<Resource<RegisterSuccessResponse>> = _registerUiState

    fun register(email: String, name: String, password: String) {
        viewModelScope.launch {
            _registerUiState.value = Resource.Loading()
            try {
                val response = authRepository.register(email, name, password)
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        val data = parseResponse<RegisterSuccessResponse>(responseBody)
                        _registerUiState.value = Resource.Success(data)
                    }
                } else {
                    response.errorBody()?.let { errorBody ->
                        val error = parseResponse<RegisterErrorResponse>(errorBody)
                        _registerUiState.value = Resource.Error(error.error.message)
                    }
                }
            } catch (e: Exception) {
                _registerUiState.value = Resource.Error(e.message.toString())
            }
        }
    }
}