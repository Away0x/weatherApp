package com.away0x.weather.app

import com.away0x.weather.app.data.PlaceRepository
import com.away0x.weather.app.data.WeatherRepository
import com.away0x.weather.app.data.db.CoolWeatherDatabase
import com.away0x.weather.app.data.network.WeatherNetwork
import com.away0x.weather.app.ui.area.AreaViewModelFactory
import com.away0x.weather.app.ui.weather.WeatherViewModelFactory

object InjectorUtils {

    private fun getPlaceRepository() = PlaceRepository
        .getInstance(CoolWeatherDatabase.getPlaceDao(), WeatherNetwork.getInstance())

    fun getWeatherRepository() = WeatherRepository
        .getInstance(CoolWeatherDatabase.getWeatherDao(), WeatherNetwork.getInstance())

    fun getAreaViewModelFactory() = AreaViewModelFactory(getPlaceRepository())

    fun getWeatherModelFactory() = WeatherViewModelFactory(getWeatherRepository())

}