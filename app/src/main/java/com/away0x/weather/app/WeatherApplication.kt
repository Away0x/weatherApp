package com.away0x.weather.app

import android.app.Application
import android.content.Context
import org.litepal.LitePal

class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        LitePal.initialize(this)
        context = this
    }

    companion object {
        lateinit var context: Context
    }

}