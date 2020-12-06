package com.demoapp.encoraitunes.data.network

import com.demoapp.encoraitunes.util.AppConstant
import okhttp3.Interceptor
import okhttp3.Response


/**
 * Observes, modifies, and potentially short-circuits requests going out and the corresponding
 * responses coming back in. Typically interceptors add, remove, or transform headers on the request
 * or response
 */
class HttpInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(AppConstant.HEADER_AUTHORIZATION_KEY, AppConstant.HEADER_AUTHORIZATION_VALUE)
        return chain.proceed(request.build())
    }
}
