package com.afrinacapstone.ecocraft.ui.home.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afrinacapstone.ecocraft.preferences.UserPreference
import com.afrinacapstone.ecocraft.repository.CraftsRepository
import com.afrinacapstone.ecocraft.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPostViewModel @Inject constructor(
    private val craftsRepository: CraftsRepository,
    private val userPreference: UserPreference
) :
    ViewModel() {

    private var _deleteCraftState = MutableLiveData<Resource<String>>()
    val deleteCraftState: LiveData<Resource<String>> = _deleteCraftState

    fun deleteCraft(id: String) {
        viewModelScope.launch {
            try {
                val response = craftsRepository.deleteCraft(id)
                when {
                    response.isSuccessful -> {
                        _deleteCraftState.value = Resource.Success("Post deleted successfully")
                    }

                    response.code() == 403 -> {
                        _deleteCraftState.value =
                            Resource.Error("Unauthorized to delete this craft idea.")
                    }

                    else -> {
                        _deleteCraftState.value = Resource.Error("Failed to delete post")
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _deleteCraftState.value = Resource.Error("Failed to delete post")
            }
        }
    }

    fun getUserId() = userPreference.userId
}