package com.sample.libraryapplication.viewmodel

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.navigation.fragment.findNavController
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.R
import com.sample.libraryapplication.bo.BOCategory
import com.sample.libraryapplication.utils.ActivityWeakMapRef
import com.sample.libraryapplication.service.MyMQTTHandler
import com.sample.libraryapplication.utils.showColoredToast
import com.sample.libraryapplication.view.fragment.BookListFragment
import com.sample.libraryapplication.view.fragment.MQTTFragment
import com.sample.libraryapplication.view.MainActivity
import com.sample.libraryapplication.view.fragment.MQTTFragmentDirections
import javax.inject.Inject

class MQTTViewModel @ViewModelInject constructor(val myMQTTHandler: MyMQTTHandler): BaseViewModel() {
    val TAG = "MQTTViewModel"
    val mqttSettings = myMQTTHandler.mqttSettings()

     fun getBroker(): String?{
        return mqttSettings.get(MyMQTTHandler.BROKER_PREFS)
    }
    fun setBroker(charSequence: CharSequence){
//        Log.d(TAG, "setBroker: "+ charSequence.toString())
        mqttSettings.put(MyMQTTHandler.BROKER_PREFS, charSequence.toString())
    }
    fun getTopic(): String?{
        return mqttSettings.get(MyMQTTHandler.TOPIC_PREFS)
    }
    fun setTopic(charSequence: CharSequence){
//        Log.d(TAG, "setTopic: "+ charSequence.toString())
        mqttSettings.put(MyMQTTHandler.TOPIC_PREFS, charSequence.toString())
    }
    fun save(){
        myMQTTHandler.myPrefs.save(mqttSettings)
        Log.d(TAG, "save: " + mqttSettings.toString())
        showColoredToast("MQTT Setting Has Been Saved")
        myMQTTHandler.reConnect()
        Handler(Looper.getMainLooper()).postDelayed({
            val mainActivity = ActivityWeakMapRef.get(MainActivity.TAG) as MainActivity
            mainActivity.startActivity(Intent(mainActivity,MainActivity::class.java).addFlags(FLAG_ACTIVITY_NEW_TASK))
            mainActivity.finish();
            Log.d(BookListFragment.TAG, "Re-Routing to MainActivity")
        }, 600)
    }
}