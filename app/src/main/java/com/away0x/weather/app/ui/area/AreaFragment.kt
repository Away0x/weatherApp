package com.away0x.weather.app.ui.area

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.away0x.weather.app.InjectorUtils
import com.away0x.weather.app.R
import com.away0x.weather.app.databinding.FragmentAreaBinding

class AreaFragment(
    private val isPage: Boolean = true
) : Fragment() {

    private val viewModel by viewModels<AreaViewModel> { InjectorUtils.getAreaViewModelFactory() }
    private lateinit var binding: FragmentAreaBinding
    private lateinit var adapter: AreaAdapter
    private var progressDialog: ProgressDialog? = null
    private var onSelectedAreaListener: OnSelectedAreaListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAreaBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = AreaAdapter(requireContext(), R.layout.simple_item, viewModel.dataList)
        binding.listView.adapter = adapter

        observeViewModel()
    }
    
    fun setOnSelectedAreaListener(listener: OnSelectedAreaListener) {
        this.onSelectedAreaListener = listener
    }

    private fun observeViewModel() {
        viewModel.apply {
            currentLevel.observe(viewLifecycleOwner, Observer {
                when (it) {
                    LEVEL_PROVINCE -> {
                        binding.titleText.text = "中国"
                        binding.backButton.visibility = View.GONE
                    }
                    LEVEL_CITY -> {
                        binding.titleText.text = selectedProvince?.provinceName
                        binding.backButton.visibility = View.VISIBLE
                    }
                    LEVEL_COUNTY -> {
                        binding.titleText.text = selectedCity?.cityName
                        binding.backButton.visibility = View.VISIBLE
                    }
                }
            })

            dataChanged.observe(viewLifecycleOwner, Observer {
                adapter.notifyDataSetChanged()
                binding.listView.setSelection(0)
                closeProgressDialog()
            })

            isLoading.observe(viewLifecycleOwner, Observer {
                if (it) showProgressDialog()
                else closeProgressDialog()
            })

            areaSelected.observe(viewLifecycleOwner, Observer {
                if (it && selectedCounty != null) {
                    if (isPage) {
                        // 当前是一个 Page 展示的
                        val action = AreaFragmentDirections.actionAreaFragmentToWeatherFragment(selectedCounty?.weatherId)
                        findNavController().navigate(action)
                    } else {
                        // 当前是作为一个 fragment 组件展示的
                        onSelectedAreaListener?.onSelect(selectedCounty!!)
                    }

                    areaSelected.value = false
                }
            })

            if (dataList.isEmpty()) getProvinces()
        }
    }

    private fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(activity)
            progressDialog?.setMessage("正在加载...")
            progressDialog?.setCanceledOnTouchOutside(false)
        }
        progressDialog?.show()
    }

    private fun closeProgressDialog() {
        progressDialog?.dismiss()
    }

    companion object {
        fun newInstance(): AreaFragment {
            return AreaFragment(false)
        }
    }

}