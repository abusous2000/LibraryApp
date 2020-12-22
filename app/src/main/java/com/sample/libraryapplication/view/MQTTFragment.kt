package com.sample.libraryapplication.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sample.libraryapplication.R
import com.sample.libraryapplication.databinding.MqttFragmentBinding
import com.sample.libraryapplication.viewmodel.MQTTViewModel

class MQTTFragment : Fragment() {
    companion object {
        val TAG = "MQTTFragment"
        fun newInstance() = MQTTFragment()
    }
    private lateinit var viewModel: MQTTViewModel
    private lateinit var binding: MqttFragmentBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = MqttFragmentBinding.inflate(layoutInflater,container,false)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MQTTViewModel::class.java)
        reenterTransition = true
    }
}