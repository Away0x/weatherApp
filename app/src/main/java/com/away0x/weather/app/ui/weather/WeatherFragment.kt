package com.away0x.weather.app.ui.weather

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.away0x.weather.app.InjectorUtils
import com.away0x.weather.app.R
import com.away0x.weather.app.data.model.place.County
import com.away0x.weather.app.databinding.FragmentWeatherBinding
import com.away0x.weather.app.ui.area.AreaFragment
import com.away0x.weather.app.ui.area.OnSelectedAreaListener
import kotlinx.android.synthetic.main.layout_weather_title.*

class WeatherFragment : Fragment() {

    private val navigationArgs by navArgs<WeatherFragmentArgs>()
    private val viewModel by viewModels<WeatherViewModel> { InjectorUtils.getWeatherModelFactory() }
    private lateinit var binding: FragmentWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.bgResId = R.color.colorPrimary
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = requireActivity().window
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        }

        val areaFragment = AreaFragment.newInstance()
        areaFragment.setOnSelectedAreaListener(object : OnSelectedAreaListener {
            override fun onSelect(selectedCounty: County) {
                binding.drawerLayout.closeDrawers()
                viewModel.weatherId = selectedCounty.weatherId
                viewModel.refreshWeather()
            }
        })

        requireActivity().supportFragmentManager.beginTransaction().apply {
            add(binding.areaFragmentContainer.id, areaFragment)
            commit()
        }

        navButton.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        viewModel.apply {
            weatherId = if (isWeatherCached()) {
                getCachedWeather().basic.weatherId
            } else {
                navigationArgs.weatherId ?: ""
            }

            getWeather()
        }
    }

}