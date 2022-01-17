package com.sample.libraryapplication

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
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