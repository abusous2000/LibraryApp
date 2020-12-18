package com.sample.libraryapplication.utils

import android.content.Context
import android.content.SharedPreferences

class MyPrefsRespository(val context: Context)  {
    val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
    }
    companion object {
        const val SHARED_PREFS = "SHARED_PREFS"
    }
    fun save(map: Map<String, String>) {

        with(sharedPref.edit()) {
            map.entries.forEach({
                putString(it.key,it.value)
            })
            commit()
        }
    }
    fun contains(key: String): Boolean{
        return sharedPref.contains(key)
    }
    fun getString(key: String, defaultValue: String?=null): String {
        return sharedPref.getString(key,defaultValue)!!;
    }
    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPref.getInt(key,defaultValue)
    }
    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPref.getBoolean(key,defaultValue)
    }
}
