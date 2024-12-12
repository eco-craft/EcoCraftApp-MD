package com.afrinacapstone.ecocraft.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afrinacapstone.ecocraft.api.response.GetCraftsResponse
import com.afrinacapstone.ecocraft.api.response.GetUserResponse
import com.afrinacapstone.ecocraft.model.Craft
import com.afrinacapstone.ecocraft.repository.CraftsRepository
import com.afrinacapstone.ecocraft.model.toCrafts
import com.afrinacapstone.ecocraft.preferences.UserPreference
import com.afrinacapstone.ecocraft.repository.ProfileRepository
import com.afrinacapstone.ecocraft.util.Resource
import com.afrinacapstone.ecocraft.util.parseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val craftsRepository: CraftsRepository,
    private val profileRepository: ProfileRepository,
    private val userPreference: UserPreference
) :
    ViewModel() {

    private val _craftsUiState = MutableLiveData<Resource<List<Craft>>>()
    val craftsUiState: LiveData<Resource<List<Craft>>> = _craftsUiState

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            try {
                val response = profileRepository.getUser()
                if (response.isSuccessful) {
                    response.body()?.let {
                        val user = parseResponse<GetUserResponse>(it)
                        userPreference.saveUserId(user.data.id)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getCrafts(title: String) {
        viewModelScope.launch {
            _craftsUiState.value = Resource.Loading()
            try {
                val response = craftsRepository.getCrafts(title)

                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        val data = parseResponse<GetCraftsResponse>(responseBody)
                        val crafts = data.toCrafts()
                        if (crafts.isEmpty()) _craftsUiState.value =
                            Resource.Error("Crafts not found")
                        else
                            _craftsUiState.value = Resource.Success(crafts)
                    }
                } else {
                    _craftsUiState.value = Resource.Error(response.message())
                }
            } catch (e: Exception) {
                _craftsUiState.value = Resource.Error(e.message.toString())
            }
        }
    }
}
