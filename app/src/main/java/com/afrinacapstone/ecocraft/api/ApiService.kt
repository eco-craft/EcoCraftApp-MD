package com.afrinacapstone.ecocraft.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @Headers("Accept: application/json")
    @POST("users")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<ResponseBody>

    @POST("users/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<ResponseBody>

    @Multipart
    @Headers("Accept: application/json")
    @POST("predictions")
    suspend fun postPrediction(
        @Part materialImage: MultipartBody.Part
    ): Response<ResponseBody>

    @GET("predictions")
    suspend fun getPredictionHistory(): Response<ResponseBody>

    @GET("predictions/{id}")
    suspend fun getPrediction(
        @Path("id") id: String
    ): Response<ResponseBody>

    @GET("crafts")
    suspend fun getCrafts(@Query("title") title: String): Response<ResponseBody>

    @Multipart
    @Headers("Accept: application/json")
    @POST("crafts")
    suspend fun postCraft(
        @Part("title") title: RequestBody,
        @Part("materials[]") materials: RequestBody,
        @Part("steps[]") description: RequestBody,
        @Part craftImage: MultipartBody.Part
    ): Response<ResponseBody>

    @Headers("Accept: application/json")
    @POST("crafts")
    suspend fun postCraft(
        @Body requestBody: RequestBody
    ): Response<ResponseBody>

    @DELETE("crafts/{id}")
    suspend fun deleteCraft(
        @Path("id") id: String
    ): Response<ResponseBody>

    @GET("crafts/{id}")
    suspend fun getCraft(
        @Path("id") id: String
    ): Response<ResponseBody>

    @Headers("Accept: application/json")
    @PATCH("crafts/{id}")
    suspend fun updateUserProfile(
        @Path("id") id: String,
        @Body userRequestBody: RequestBody
    ): Response<ResponseBody>

    @GET("users/current")
    suspend fun getUser(): Response<ResponseBody>

    @Headers("Accept: application/json")
    @PATCH("users/current")
    suspend fun updateUserProfile(
        @Body userRequestBody: RequestBody
    ): Response<ResponseBody>

    @Headers("Accept: application/json")
    @PATCH("users/current")
    suspend fun updatePassword(
        @Body userRequestBody: RequestBody
    ): Response<ResponseBody>
}