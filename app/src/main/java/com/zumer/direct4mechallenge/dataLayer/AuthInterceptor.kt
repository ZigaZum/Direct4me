package com.zumer.direct4mechallenge.dataLayer

import okhttp3.Interceptor
import okhttp3.Response

private const val API_KEY = "d658ff009bc647a3baa92f9285214e0f"

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader("Authorization", API_KEY)

        return chain.proceed(requestBuilder.build())
    }
}