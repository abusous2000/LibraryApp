package com.sample.libraryapplication.utils

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.bo.BOBook
import com.sample.libraryapplication.bo.BOCategory
import com.sample.libraryapplication.database.DBPopulator
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity
import com.sample.libraryapplication.view.MainActivity
import org.eclipse.paho.client.mqttv3.MqttMessage
import javax.inject.Inject
import javax.inject.Singleton

data class TopicHandler(val topic: String, val messageCallBack: ((topic: String, message: MqttMessage) -> Unit)? = null){
}
@Singleton
class MyMQTTHandler @Inject constructor() : MqttClientHelper() {
    companion object {
        const val TOPIC_PREFS = "TOPIC_PREFS"
        const val BROKER_PREFS = "BROKER_PREFS"

        private val TAG = "MyMQTTHandler"
    }
    private val default_broker: String = "tcp://broker.hivemq.com"
    private val default_topic: String = "abusous2000/myTopic"
    var topicHandlers = mutableSetOf<TopicHandler>()
    lateinit var broker: String
    lateinit var topic: String
    val myPrefs: MyPrefsRespository by lazy {
        MyPrefsRespository(context)
    }
    init{
        LibraryApplication.instance.libraryComponent.inject(this)
    }
    fun mqttSettings(): MutableMap<String, String> {
        var mqttSettings = mutableMapOf<String,String>()

        mqttSettings.put(BROKER_PREFS,myPrefs.getString(BROKER_PREFS,"N/A"))
        mqttSettings.put(TOPIC_PREFS,myPrefs.getString(TOPIC_PREFS,"N/A"))

        return mqttSettings
    }
    fun connect(){
        if ( myPrefs.contains(BROKER_PREFS) == false){
            myPrefs.save(mapOf<String,String>(BROKER_PREFS to default_broker, TOPIC_PREFS to default_topic));
        }

        topicHandlers.add( TopicHandler(myPrefs.getString(TOPIC_PREFS)))
        connect(myPrefs.getString(BROKER_PREFS))
    }
    override fun onConnectComplete(reconnect: Boolean, serverURI: String?){
        topicHandlers.forEach({
            subscribeTopic(it.topic)
        })
    }
    fun reConnect(){
        close()
        connect()
    }
    override fun onConnectionLost(cause: Throwable?){
    }

    val gson = GsonBuilder().serializeNulls().create()
    val bookActionEvents = arrayOf<String>(ActionEvent.INSERT_BOOK_AE,ActionEvent.UPDATE_BOOK_AE,ActionEvent.DELETE_BOOK_AE)
    val categoryActionEvents = arrayOf<String>(ActionEvent.INSERT_CATEGORY_AE,ActionEvent.UPDATE_CATEGORY_AE,ActionEvent.DELETE_CATEGORY_AE)
    @Inject
    lateinit var boCategory: BOCategory
    @Inject
    lateinit var boBook: BOBook
    override fun onDefaultMessageArrived(topic: String?, message: MqttMessage?) {
        val json = String(message?.payload!!)
        Log.d(MainActivity.TAG, "Receive MQTTMessage: $json---> on Topic:$topic")

        var th = topicHandlers.find { it.topic.equals(topic) }
        //route to topic's message handler if one was provided, else use the default implementation
        if ( th?.messageCallBack != null){
            th.messageCallBack!!(topic!!,message)
            return;
        }
        try {
            val actionEvent = gson.fromJson(json, ActionEvent::class.java)
            when (actionEvent.actionEvent) {
                in bookActionEvents -> {
                    val book = gson.fromJson(actionEvent.data, BookEntity::class.java)

                    Log.d(MainActivity.TAG, "book: \"${book.bookName}\" has been received")
                    book.resourceId = if (book.resourceId >=0 && book.resourceId <DBPopulator.bookResources.size)
                                           DBPopulator.bookResources[book.resourceId]
                                      else
                                            book.resourceId
                    when (actionEvent.actionEvent) {
                        ActionEvent.INSERT_BOOK_AE->boBook.setEntity(book).insert()
                        ActionEvent.UPDATE_BOOK_AE->boBook.setEntity(book).update()
                        ActionEvent.DELETE_BOOK_AE->boBook.setEntity(book).delete()
                    }
                }
                in categoryActionEvents -> {
                    val category = gson.fromJson(actionEvent.data, CategoryEntity::class.java)

                    Log.d(MainActivity.TAG, "category:\"${category.categoryName}\" has been received")
                    when (actionEvent.actionEvent) {
                        ActionEvent.INSERT_CATEGORY_AE-> {
                            with(boCategory) {
                                setEntity(category).insert()
                                //This is a hack, for some reason observer of categories are not notified only once
                                categories.value?.add(category)
                                categoryListUpdated.postValue(true)
                            }
                        }
                        ActionEvent.UPDATE_CATEGORY_AE->boCategory.setEntity(category).update()
                        ActionEvent.DELETE_CATEGORY_AE->boCategory.setEntity(category).delete()
                    }
                }
                else -> Log.d(MainActivity.TAG, "unknown action event")
            }
        }
        catch (e: Exception){
            Log.e(MainActivity.TAG, "onMessageArrived: "+e.message,e )
        }
    }
}