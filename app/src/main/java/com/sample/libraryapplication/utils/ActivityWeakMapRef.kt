package com.sample.libraryapplication.utils

import android.app.Activity
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference
import java.util.*

class ActivityWeakMapRef{
    companion object {
        private val weakMap = WeakHashMap<String, Any>()

        fun put(key: String, value: Any){
            weakMap.put(key,value)
        }
        fun get(key: String): Any?{
            return weakMap.get(key)
        }
    }
}
