package com.afrinacapstone.ecocraft.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afrinacapstone.ecocraft.api.response.LoginErrorResponse
import com.afrinacapstone.ecocraft.api.response.LoginSuccessResponse
import com.afrinacapstone.ecocraft.preferences.UserPreference
import com.afrinacapstone.ecocraft.repository.AuthRepository
import com.afrinacapstone.ecocraft.util.Resource
import com.afrinacapstone.ecocraft.util.parseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreference: UserPreference
) : ViewModel() {

    private val _loginUiState = MutableLiveData<Resource<LoginSuccessResponse>>()
    val loginUiState: MutableLiveData<Resource<LoginSuccessResponse>> = _loginUiState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginUiState.value = Resource.Loading()
            try {
                val response = authRepository.login(email, password)
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        val data = parseResponse<LoginSuccessResponse>(responseBody)
                        userPreference.saveAccessToken(data.data.accessToken)
                        _loginUiState.value = Resource.Success(data)
                    }
                } else {
                    response.errorBody()?.let { errorBody ->
                        val error = parseResponse<LoginErrorResponse>(errorBody)
                        _loginUiState.value = Resource.Error(error.error.message)
                    }
                }
            } catch (e: Exception) {
                _loginUiState.value = Resource.Error(e.message.toString())
            }
        }
    }

    fun saveUserLoginStatus(isLoggedIn: Boolean) {
        viewModelScope.launch {
            userPreference.saveLoginStatus(isLoggedIn)
        }
    }
}