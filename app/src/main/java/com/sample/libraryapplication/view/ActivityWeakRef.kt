package com.sample.libraryapplication.view

import android.app.Activity
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

class ActivityWeakRef{
    companion object {
        val activityMap = mutableMapOf<String, WeakReference<Activity>>()
        val fragmentMap = mutableMapOf<String, WeakReference<Fragment>>()

        fun updateActivity(key: String, activity: Activity) {
            activityMap.put(key, WeakReference(activity))
        }
        fun updateFragment(key: String, fragment: Fragment) {
            fragmentMap.put(key, WeakReference(fragment))
        }
    }
}
