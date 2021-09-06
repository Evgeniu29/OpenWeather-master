package com.suleimanazhari.openweather.ui

import androidx.lifecycle.ViewModel
import com.suleimanazhari.openweather.data.network.NetworkDataSource

class WeatherViewModel(
    private val networkDataSource: NetworkDataSource
) : ViewModel() {
    var town: String = ""

    val weather by lazyDeferred {
        networkDataSource.getWeather(town)
    }

    fun update(name: String):String{
        this.town = name
        return this.town
    }
}