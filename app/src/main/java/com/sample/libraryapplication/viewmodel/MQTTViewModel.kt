package com.sample.libraryapplication.viewmodel

import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.utils.ActivityWeakMapRef
import com.sample.libraryapplication.utils.MyMQTTHandler
import com.sample.libraryapplication.view.BookListFragment
import com.sample.libraryapplication.view.MQTTFragment
import com.sample.libraryapplication.view.MainActivity
import javax.inject.Inject

class MQTTViewModel: BaseViewModel() {
    val TAG = "MQTTViewModel"
    @Inject
    lateinit var myMQTTHandler: MyMQTTHandler
    lateinit var mqttSettings:  MutableMap<String,String>

    override fun registerWithComponent() {
        LibraryApplication.instance.libraryComponent.inject( this)

        mqttSettings = myMQTTHandler.mqttSettings()
    }

    fun getBroker(): String?{
        return mqttSettings.get(MyMQTTHandler.BROKER_PREFS)
    }
    fun setBroker(charSequence: CharSequence){
        Log.d(TAG, "setBroker: ")
        mqttSettings.put(MyMQTTHandler.BROKER_PREFS,charSequence.toString())
    }
    fun getTopic(): String?{
        return mqttSettings.get(MyMQTTHandler.TOPIC_PREFS)
    }
    fun setTopic(charSequence: CharSequence){
        Log.d(TAG, "setTopic: ")
        mqttSettings.put(MyMQTTHandler.TOPIC_PREFS,charSequence.toString())
    }
    fun save(){
        myMQTTHandler.myPrefs.save(mqttSettings)
        Log.d(TAG, "save: " + mqttSettings.toString())
        var info = "MQTT Setting Has Been Saved"
        var toast = Toast.makeText((ActivityWeakMapRef.get(MQTTFragment.TAG) as MQTTFragment).activity?.baseContext,
                                   Html.fromHtml("<font color='red' ><b>" + info + "</b></font>"), Toast.LENGTH_LONG)
        toast.show()
        myMQTTHandler.close()
        myMQTTHandler.connect((ActivityWeakMapRef.get(MQTTFragment.TAG) as MQTTFragment).requireContext())

        Handler(Looper.getMainLooper()).postDelayed({
            (ActivityWeakMapRef.get(MainActivity.TAG) as MainActivity).selectItem(MainActivity.BOOK_LIST_MENU_NDX)
             Log.d(BookListFragment.TAG, "Re-Routing to MainActivity")
        }, 3000)

    }
}