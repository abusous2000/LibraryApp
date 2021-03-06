package com.sample.libraryapplication.viewmodel

import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.widget.Toast
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.utils.ActivityWeakMapRef
import com.sample.libraryapplication.service.MyMQTTHandler
import com.sample.libraryapplication.view.fragment.BookListFragment
import com.sample.libraryapplication.view.fragment.MQTTFragment
import com.sample.libraryapplication.view.MainActivity
import javax.inject.Inject

class MQTTViewModel: BaseViewModel() {
    val TAG = "MQTTViewModel"
    @Inject
    lateinit var myMQTTHandler: MyMQTTHandler
    lateinit var mqttSettings:  MutableMap<String, String>

    override fun registerWithComponent() {
        LibraryApplication.instance.libraryComponent.inject(this)

        mqttSettings = myMQTTHandler.mqttSettings()
    }

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
        val info = "MQTT Setting Has Been Saved"
        val mqttFragment = ActivityWeakMapRef.get(MQTTFragment.TAG) as MQTTFragment
        var toast = Toast.makeText(mqttFragment.activity?.baseContext,
                     Html.fromHtml("<font color='red' ><b>" + info + "</b></font>", Html.FROM_HTML_MODE_LEGACY), Toast.LENGTH_LONG)
        toast.show()
        myMQTTHandler.reConnect()

        Handler(Looper.getMainLooper()).postDelayed({
            var mainActivity = ActivityWeakMapRef.get(MainActivity.TAG) as MainActivity

            mainActivity.selectItem(MainActivity.BOOK_LIST_MENU_NDX)
            Log.d(BookListFragment.TAG, "Re-Routing to MainActivity")
        }, 600)
    }
}