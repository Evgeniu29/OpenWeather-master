package com.suleimanazhari.openweather.data.network

import androidx.lifecycle.LiveData
import com.suleimanazhari.openweather.data.model.WeatherResponse

interface NetworkDataSource {
    val downloadedWeather: LiveData<WeatherResponse>

    suspend fun getWeather(
            location: String
    ): LiveData<WeatherResponse>
}