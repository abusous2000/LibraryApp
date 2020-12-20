package com.sample.libraryapplication

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.sample.libraryapplication.dagger.DaggerLibraryComponent
import com.sample.libraryapplication.dagger.LibraryComponent
import com.sample.libraryapplication.dagger.module.RoomDatabaseModule

class LibraryApplication : Application() {

    companion object {
        lateinit var instance: LibraryApplication
        lateinit var roomDatabaseModule : RoomDatabaseModule
    }
    lateinit var libraryComponent: LibraryComponent

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        instance = this
        roomDatabaseModule = RoomDatabaseModule(this)
        libraryComponent = DaggerLibraryComponent
            .builder()
            .roomDatabaseModule(roomDatabaseModule)
            .build()
    }
}