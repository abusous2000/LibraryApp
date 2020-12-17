package com.sample.libraryapplication.utils

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.bo.BOBook
import com.sample.libraryapplication.bo.BOCategory
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity
import com.sample.libraryapplication.view.BookListActivity
import org.eclipse.paho.client.mqttv3.MqttMessage
import javax.inject.Inject

data class TopicHandler(val topic: String, val messageCallBack: ((topic: String, message: MqttMessage) -> Unit)? = null){
}
class MyMQTTHandler @Inject constructor() : MqttClientHelper() {
    companion object {
        private val TAG = "MyMQTTHandler"
    }
    var topicHandlers = mutableSetOf<TopicHandler>()
    lateinit var broker: String
    lateinit var topic: String
    val prefs: PrefsRespository by lazy {
        PrefsRespository(context)
    }
    val gson = GsonBuilder().serializeNulls().create()
    val bookActionEvents = arrayOf<String>(ActionEvent.INSERT_BOOK_AE,ActionEvent.UPDATE_BOOK_AE,ActionEvent.DELETE_BOOK_AE)
    val categoryActionEvents = arrayOf<String>(ActionEvent.INSERT_CATEGORY_AE,ActionEvent.UPDATE_CATEGORY_AE,ActionEvent.DELETE_CATEGORY_AE)
    @Inject
    lateinit var boCategory: BOCategory
    @Inject
    lateinit var boBook: BOBook

    init{
        LibraryApplication.instance.libraryComponent.inject(this)
    }

    fun connect(context: Context){
        this.context = context
        var prefMap = mapOf<String,String>(PrefsRespository.Companion.BROKER_PREFS to prefs.getBroker(),
                                                            PrefsRespository.Companion.TOPIC_PREFS to prefs.getTopic())
//        prefs.saveBroker(prefMap)
        topicHandlers.add( TopicHandler(prefs.getTopic()))
        super.connect(context,prefs.getBroker())
    }
    override fun onConnectComplete(reconnect: Boolean, serverURI: String?){
        topicHandlers.forEach({
            subscribeTopic(it.topic)
        })
    }

    override fun onConnectionLost(cause: Throwable?){
    }
    override fun onDefaultMessageArrived(topic: String?, message: MqttMessage?) {
        val json = String(message?.payload!!)
        Log.d(BookListActivity.TAG, "Receive MQTTMessage: $json---> on Topic:$topic")

        var th = topicHandlers.find { it.topic.equals(topic) }
        if ( th?.messageCallBack != null){
            th.messageCallBack!!(topic!!,message)
            return;
        }
        try {
            val actionEvent = gson.fromJson(json, ActionEvent::class.java)
            when (actionEvent.actionEvent) {
                in bookActionEvents -> {
                    val book = gson.fromJson(actionEvent.data, BookEntity::class.java)

                    Log.d(BookListActivity.TAG, "book: \"${book.bookName}\" has been received")
                    when (actionEvent.actionEvent) {
                        ActionEvent.INSERT_BOOK_AE->boBook.setEntity(book).insert()
                        ActionEvent.UPDATE_BOOK_AE->boBook.setEntity(book).update()
                        ActionEvent.DELETE_BOOK_AE->boBook.setEntity(book).delete()
                    }
                }
                in categoryActionEvents -> {
                    val category = gson.fromJson(actionEvent.data, CategoryEntity::class.java)

                    Log.d(BookListActivity.TAG, "category:\"${category.categoryName}\" has been received")
                    when (actionEvent.actionEvent) {
                        ActionEvent.INSERT_CATEGORY_AE-> {
                            boCategory.setEntity(category).insert()
                            boCategory.categories.value?.add(category)
                            boCategory.categoryListUpdated.postValue(true)
                        }
                        ActionEvent.UPDATE_CATEGORY_AE->boCategory.setEntity(category).update()
                        ActionEvent.DELETE_CATEGORY_AE->boCategory.setEntity(category).delete()
                    }
                }
                else -> Log.d(BookListActivity.TAG, "unknown action event")
            }
        }
        catch (e: Exception){
            Log.e(BookListActivity.TAG, "onMessageArrived: "+e.message,e )
        }
    }
}