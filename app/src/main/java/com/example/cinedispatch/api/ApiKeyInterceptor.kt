package com.example.cinedispatch.api

import com.example.cinedispatch.api.Constants.api_key
import com.example.cinedispatch.api.Constants.token
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request=chain.request()
        val UrL=request.url.newBuilder().addQueryParameter("apiKey",api_key).build()
        val newRequest=request.newBuilder().url(UrL).addHeader("accept", "application/json").addHeader("authorization","Bearer $token").build()
        return chain.proceed(newRequest)
    }
}