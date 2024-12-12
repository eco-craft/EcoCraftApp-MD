package com.afrinacapstone.ecocraft.repository

import com.afrinacapstone.ecocraft.api.ApiService
import okhttp3.RequestBody
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val api: ApiService) {

    suspend fun getUser() = api.getUser()

    suspend fun updateUserProfile(requestBody: RequestBody) = api.updateUserProfile(requestBody)

    suspend fun updatePassword(requestBody: RequestBody) = api.updatePassword(requestBody)
}