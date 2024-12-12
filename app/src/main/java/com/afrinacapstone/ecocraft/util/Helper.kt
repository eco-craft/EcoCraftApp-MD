package com.afrinacapstone.ecocraft.util

import com.google.gson.Gson
import okhttp3.ResponseBody

inline fun <reified T> parseResponse(responseBody: ResponseBody): T {
    val gson = Gson()
    return gson.fromJson(responseBody.charStream(), T::class.java)
}
