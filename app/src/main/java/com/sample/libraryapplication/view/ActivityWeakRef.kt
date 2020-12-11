package com.sample.libraryapplication.view

import android.app.Activity
import java.lang.ref.WeakReference

class ActivityWeakRef{
    companion object {
        val activityMap = mutableMapOf<String, WeakReference<Activity>>()

        fun updateActivity(key: String, activity: Activity) {
            activityMap.put(key, WeakReference(activity))
        }
    }
}
