package com.afrinacapstone.ecocraft.repository

import com.afrinacapstone.ecocraft.api.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CraftsRepository @Inject constructor(private val api: ApiService) {

    suspend fun getCrafts(title: String) = api.getCrafts(title)

    suspend fun postCraft(
        title: RequestBody,
        materials: RequestBody,
        description: RequestBody,
        craftImage: MultipartBody.Part
    ) = api.postCraft(title, materials, description, craftImage)

    suspend fun deleteCraft(id: String) = api.deleteCraft(id)

    suspend fun updateUserProfile(id: String, userRequestBody: RequestBody) =
        api.updateUserProfile(id, userRequestBody)

    suspend fun getCraft(id: String) = api.getCraft(id)

    suspend fun postCraft(requestBody: RequestBody) = api.postCraft(requestBody)
}