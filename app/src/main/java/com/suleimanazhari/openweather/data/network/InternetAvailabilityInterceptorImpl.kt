package com.suleimanazhari.openweather.data.network

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response

class InternetAvailabilityInterceptorImpl(
        context: Context
) : InternetAvailabilityInterceptor {

    private val appContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected())
            throw NoInternetException()
        return chain.proceed(chain.request())
    }

    private fun isConnected(): Boolean {
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        return connectivityManager.activeNetworkInfo != null
                && connectivityManager.activeNetworkInfo.isConnected
    }
}