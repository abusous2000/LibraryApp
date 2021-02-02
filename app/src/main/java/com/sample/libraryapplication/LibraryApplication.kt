package com.sample.libraryapplication

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LibraryApplication : Application() {

    companion object {
        lateinit var instance: LibraryApplication
    }
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        instance = this
    }
}