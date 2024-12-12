package com.afrinacapstone.ecocraft.repository

import com.afrinacapstone.ecocraft.api.ApiService
import com.afrinacapstone.ecocraft.api.LoginRequest
import com.afrinacapstone.ecocraft.api.RegisterRequest
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(private val api: ApiService) {

    suspend fun login(email: String, password: String): Response<ResponseBody> {
        val loginRequest = LoginRequest(email, password)
        return api.login(loginRequest)
    }

    suspend fun register(email: String, name: String, password: String): Response<ResponseBody> {
        val registerRequest = RegisterRequest(name, email, password)
        return api.register(registerRequest)
    }
}