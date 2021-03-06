package com.sample.libraryapplication.service

import android.util.Log
import com.sample.libraryapplication.LibraryApplication
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

abstract class MqttClientHelper() {
    var client: MqttAndroidClient? = null
    val context = LibraryApplication.instance.applicationContext

     companion object {
        const val TAG = "MqttClient"
        fun generateClientId(): String? {
            return TAG +"S4E"+ System.currentTimeMillis();
        }
    }
    //override if you which to handle these call backs upon connecting
    abstract fun onConnectComplete(reconnect: Boolean, serverURI: String?)
    open fun onConnectionLost(cause: Throwable?){}
    open fun onDefaultMessageArrived(topic: String?, message: MqttMessage?){}
    open fun onDeliveryComplete(token: IMqttDeliveryToken?){}
    fun isInitalizdedAndConnected(): Boolean{
        return client != null && client?.isConnected!!
    }
    fun connect(broker: String) {
        var cb = object: MqttCallbackExtended{
            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                Log.d(TAG, "connectComplete: ")
                onConnectComplete(reconnect,serverURI)
            }
           override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.d(TAG, "messageArrived: ")
               onDefaultMessageArrived(topic,message)
            }
            override fun connectionLost(cause: Throwable?) {
                Log.d(TAG, "connectionLost: ")
                onConnectionLost(cause)
            }
            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.d(TAG, "deliveryComplete: ")
                onDeliveryComplete(token)
            }
        }
        if ( isInitalizdedAndConnected() ){
            Log.d(TAG, "connect: no need to continue, Singelton MQTT already initalized and connected")
            return;
        }
        client =  MqttAndroidClient(context, broker, generateClientId())
        client?.setCallback(cb)
        var mqttConnectOptions = MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        try {
            client?.connect(mqttConnectOptions,null,object: IMqttActionListener{
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "onSuccess: MQTT Client connected to $broker")
                }
               override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                   Log.e(TAG, "Failed to connect to $broker",exception)
                }
            })
        }
        catch(t: Throwable){
            Log.e(TAG, "MQTT connection failure: ${t.message}", t)
        }
    }

     open fun publishMessage(topic: String, msg: String) {
        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            client?.publish(topic, message.payload, 0, true)
            Log.d(TAG, "$msg published to $topic")
        } catch (e: MqttException) {
            Log.d(TAG, "Error Publishing to $topic: " + e.message)
            e.printStackTrace()
        }

    }

    open fun subscribeTopic(topic: String, qos: Int = 0) {
        client?.subscribe(topic, qos)?.actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                Log.d(TAG, "Subscribed to $topic")
            }

            override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                Log.e(TAG, "Failed to subscribe to $topic",exception)
            }
        }
    }

    fun close() {
        client?.apply {
            unregisterResources()
            close()
        }
        client = null
    }

    fun disconnect() {
        if (client?.isConnected!!)
            client?.disconnect()
    }
}
