package com.sample.libraryapplication.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sample.libraryapplication.databinding.MqttFragmentBinding
import com.sample.libraryapplication.utils.ActivityWeakMapRef
import com.sample.libraryapplication.viewmodel.MQTTViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MQTTFragment : Fragment() {
    companion object {
        val TAG = "MQTTFragment"
        fun newInstance() = MQTTFragment()
    }
    private val viewModel: MQTTViewModel by viewModels()
    private lateinit var binding: MqttFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if ( this::binding.isInitialized == false ) {
            binding = MqttFragmentBinding.inflate(layoutInflater, container, false)
        }
        binding.viewModel = viewModel
        ActivityWeakMapRef.put(TAG, this);
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}