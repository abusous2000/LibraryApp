package com.sample.libraryapplication.dagger.module

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.database.DBPopulator
import com.sample.libraryapplication.database.LibraryDatabase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Module
class RoomDatabaseModule(application: Application) {
    private var libraryApplication = application
    lateinit var libraryDatabase: LibraryDatabase

    @Inject
    lateinit var dbPopulator: DBPopulator
    companion object {
        private const val EDUCATIONAL_BOOKS_CATEGORY_ID = 1L
        private const val NOVELS_CATEGORY_ID = 2L
        private const val OTHER_BOOKS_CATEGORY_ID = 3L
        const val DB_NAME = "library_database"
    }

    private val databaseCallback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.d("RoomDatabaseModule", "onCreate")
            CoroutineScope(Dispatchers.IO).launch {
                populateDB()
            }
        }
    }
    fun populateDB(){
        LibraryApplication.instance.libraryComponent.inject(this)
        dbPopulator.populateDB()
    }


    @Singleton
    @Provides
    fun providesRoomDatabase(): LibraryDatabase {

        libraryDatabase = Room.databaseBuilder(libraryApplication, LibraryDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
//            .addCallback(databaseCallback)
//            .allowMainThreadQueries()
            .build()
        return libraryDatabase
    }

    @Singleton
    @Provides
    fun providesCategoryDAO(libraryDatabase: LibraryDatabase) = libraryDatabase.getCategoryDAO()

    @Singleton
    @Provides
    fun providesBookDAO(libraryDatabase: LibraryDatabase) = libraryDatabase.getBookDAO()
}