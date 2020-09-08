package com.away0x.weather.app.ui.area

import com.away0x.weather.app.data.model.place.County

interface OnSelectedAreaListener {
    fun onSelect(selectedCounty: County)
}