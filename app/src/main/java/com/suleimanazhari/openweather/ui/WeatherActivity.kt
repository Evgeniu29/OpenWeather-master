package com.suleimanazhari.openweather.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.suleimanazhari.openweather.R
import com.suleimanazhari.openweather.data.network.WeatherService
import com.suleimanazhari.openweather.data.network.InternetAvailabilityInterceptorImpl
import com.suleimanazhari.openweather.data.network.NetworkDataSource
import com.suleimanazhari.openweather.data.network.NetworkDataSourceImpl
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class WeatherActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    private lateinit var viewModel: WeatherViewModel

    // TODO DI with Dagger2
    private lateinit var apiService: WeatherService
    private lateinit var networkDataSource: NetworkDataSource
    private lateinit var weatherViewModelFactory: WeatherViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiService = WeatherService(InternetAvailabilityInterceptorImpl(this))
        networkDataSource = NetworkDataSourceImpl(apiService)
        weatherViewModelFactory = WeatherViewModelFactory(networkDataSource)

        job = Job()

        viewModel = ViewModelProviders.of(this, weatherViewModelFactory)
                .get(WeatherViewModel::class.java)


        button.setOnClickListener(View.OnClickListener {

            var name:String = textView_city?.text.toString()

            viewModel.update(name)


          bind()

        })
    }

    private fun bind() = launch {
        val temp = viewModel.weather.await()
        temp.observe(this@WeatherActivity, Observer {
            if (it == null) return@Observer

        })
    }


    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}