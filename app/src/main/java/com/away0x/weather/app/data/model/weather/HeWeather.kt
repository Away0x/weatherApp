package com.away0x.weather.app.data.model.weather

import com.google.gson.annotations.SerializedName

class HeWeather {
    @SerializedName("HeWeather")
    var weather: List<Weather>? = null
}