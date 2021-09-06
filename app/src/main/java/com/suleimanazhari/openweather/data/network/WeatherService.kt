package com.suleimanazhari.openweather.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.suleimanazhari.openweather.BuildConfig
import com.suleimanazhari.openweather.data.model.WeatherResponse
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "f113a1e2b207c643c6e1ce854a9a5b78"

interface WeatherService {

    @GET("weather")
    fun getWeather(
            @Query("q") location: String,
            @Query("units") unit: String = "metric",
            @Query("lang") lang: String = "en"

    ): Deferred<WeatherResponse>

    companion object {
        operator fun invoke(
                internetAvailabilityInterceptor: InternetAvailabilityInterceptor
        ): WeatherService {

            // Interceptor to inject API key in each request
            val requestInterceptor = Interceptor { chain ->

                val url = chain.request()
                        .url()
                        .newBuilder()
                        .addQueryParameter("APPID", API_KEY)
                        .build()
                val request = chain.request()
                        .newBuilder()
                        .url(url)
                        .build()

                return@Interceptor chain.proceed(request)

            }

            // Add interceptor to show network log if in DEBUG build
            // Add requestInterceptor (API Key injector)
            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(requestInterceptor)
                    .addInterceptor(internetAvailabilityInterceptor)
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                        else HttpLoggingInterceptor.Level.NONE
                    })
                    .build()

            return Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("https://api.openweathermap.org/data/2.5/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .build()
                    .create(WeatherService::class.java)
        }
    }
}