package com.sample.libraryapplication

import android.app.Application
import com.sample.libraryapplication.dagger.component.DaggerLibraryComponent
import com.sample.libraryapplication.dagger.component.LibraryComponent
import com.sample.libraryapplication.dagger.module.RoomDatabaseModule

class LibraryApplication : Application() {

    companion object {
        lateinit var instance: LibraryApplication
        lateinit var roomDatabaseModule : RoomDatabaseModule
    }
    lateinit var libraryComponent: LibraryComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        roomDatabaseModule = RoomDatabaseModule(this)
        libraryComponent = DaggerLibraryComponent
            .builder()
            .roomDatabaseModule(roomDatabaseModule)
            .build()
    }
}