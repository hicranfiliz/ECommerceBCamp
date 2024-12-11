package com.example.ecommercebcamp.retrofit

import retrofit2.Response

open class BaseRepository {
    protected suspend fun <T> apiCall(apiCall: suspend () -> Response<T>): T {
        val response = apiCall.invoke()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Response not found")
        } else {
            throw Exception("Failed with error: ${response.errorBody()?.string()} and code: ${response.code()}")
        }
    }
}