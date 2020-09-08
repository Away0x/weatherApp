package com.away0x.weather.app.utils

import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.away0x.weather.app.R
import com.away0x.weather.app.data.model.weather.Weather
import com.away0x.weather.app.databinding.ForecastItemBinding
import com.bumptech.glide.Glide

@BindingAdapter("loadBingPic")
fun ImageView.loadBingPic(url: String?) {
    if (url != null) Glide.with(context).load(url).into(this)
}

@BindingAdapter("colorSchemeResources")
fun SwipeRefreshLayout.colorSchemeResources(resId: Int) {
    setColorSchemeResources(resId)
}

@BindingAdapter("showForecast")
fun LinearLayout.showForecast(weather: Weather?) = weather?.let {
    removeAllViews()
    for (forecast in it.forecastList) {
        val view = LayoutInflater.from(context).inflate(R.layout.forecast_item, this, false)
        DataBindingUtil.bind<ForecastItemBinding>(view)?.forecast = forecast
        addView(view)
    }
}