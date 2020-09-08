package com.away0x.weather.app.ui.weather

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.away0x.weather.app.WeatherApplication
import com.away0x.weather.app.data.WeatherRepository
import com.away0x.weather.app.data.model.weather.Weather
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    var weather = MutableLiveData<Weather>()
    var bingPicUrl = MutableLiveData<String>()
    var refreshing = MutableLiveData<Boolean>()
    var weatherInitialized = MutableLiveData<Boolean>()

    var weatherId = ""

    fun isWeatherCached() = repository.isWeatherCached()

    fun getCachedWeather() = repository.getCachedWeather()

    fun getBgImage(refresh: Boolean = false) {
        launch({
            bingPicUrl.value = if (refresh) repository.refreshBingPic() else repository.getBingPic()
        })
    }

    fun getWeather() {
        launch({
            weather.value = repository.getWeather(weatherId)
            weatherInitialized.value = true
        })
        getBgImage()
    }

    fun refreshWeather() {
        refreshing.value = true

        launch({
            weather.value = repository.refreshWeather(weatherId)
            refreshing.value = false
            weatherInitialized.value = true
        }, {
            refreshing.value = false
        })

        getBgImage(true)
    }

    private fun launch(block: suspend () -> Unit, error: (suspend (Throwable) -> Unit)? = null) = viewModelScope.launch {
        try {
            block()
        } catch (e: Throwable) {
            if (error != null) error(e)
            Toast.makeText(WeatherApplication.context, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}

class WeatherViewModelFactory(private val repository: WeatherRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WeatherViewModel(repository) as T
    }
}