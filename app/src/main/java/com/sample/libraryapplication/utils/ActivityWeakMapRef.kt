package com.sample.libraryapplication.utils

import android.app.Activity
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference
import java.util.*

class ActivityWeakMapRef{
    companion object {
        val weakMap = WeakHashMap<String, Any>()
    }
}
