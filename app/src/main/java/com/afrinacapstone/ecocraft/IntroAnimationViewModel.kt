package com.afrinacapstone.ecocraft

import androidx.lifecycle.ViewModel
import com.afrinacapstone.ecocraft.preferences.UserPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class IntroAnimationViewModel @Inject constructor(private val userPreference: UserPreference) :
    ViewModel() {

    fun isUserLoggedIn(): Flow<Boolean> {
        return userPreference.isLoggedIn
    }

}