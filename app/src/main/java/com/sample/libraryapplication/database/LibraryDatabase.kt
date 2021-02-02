package com.sample.libraryapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sample.libraryapplication.database.dao.BookDAO
import com.sample.libraryapplication.database.dao.CategoryDAO
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity

@Database(entities = [CategoryEntity::class, BookEntity::class], version = 1,exportSchema = false)
abstract class LibraryDatabase : RoomDatabase() {

    abstract fun getCategoryDAO(): CategoryDAO
    abstract fun getBookDAO(): BookDAO

    companion object {
        const val DB_NAME = "library_database"
        @Volatile
        private var INSTANCE: LibraryDatabase? = null

        fun getInstance(appCtx: Context): LibraryDatabase {
            // Multiple threads can ask for the database at the same time, ensure we only initialize
            // it once by using synchronized. Only one thread may enter a synchronized block at a
            // time.
            synchronized(this) {
                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(appCtx, LibraryDatabase::class.java, DB_NAME)
                                    .fallbackToDestructiveMigration()
                                    //            .addCallback(databaseCallback)
                                    //            .allowMainThreadQueries()
                                    .build()
                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}