package com.suleimanazhari.openweather.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.suleimanazhari.openweather.data.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetworkDataSourceImpl(
        private val weatherService: WeatherService
) : NetworkDataSource {

    private val mutableDownloadedWeather = MutableLiveData<WeatherResponse>()
    override val downloadedWeather: LiveData<WeatherResponse>
        get() = mutableDownloadedWeather

    override suspend fun getWeather(location: String): LiveData<WeatherResponse> {
        return withContext(Dispatchers.IO) {
            fetchWeather(location)
            return@withContext downloadedWeather
        }
    }

    private suspend fun fetchWeather(location: String) {
        try {
            val fetchedWeather = weatherService
                    .getWeather(location).await()

            mutableDownloadedWeather.postValue(fetchedWeather)
        } catch (e: NoInternetException) {
            Log.d("Network", "No Internet!")
        }
    }
}