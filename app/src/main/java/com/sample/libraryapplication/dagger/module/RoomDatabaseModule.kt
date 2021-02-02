package com.sample.libraryapplication.dagger.module

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.bo.BOBook
import com.sample.libraryapplication.bo.BOCategory
import com.sample.libraryapplication.database.DBPopulator
import com.sample.libraryapplication.database.LibraryDatabase
import com.sample.libraryapplication.database.dao.BookDAO
import com.sample.libraryapplication.database.dao.CategoryDAO
import com.sample.libraryapplication.service.MyMQTTHandler
import com.sample.libraryapplication.view.BookClickHandlers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
class RoomDatabaseModule() {

    companion object {
        private const val EDUCATIONAL_BOOKS_CATEGORY_ID = 1L
        private const val NOVELS_CATEGORY_ID = 2L
        private const val OTHER_BOOKS_CATEGORY_ID = 3L
        const val DB_NAME = "library_database"
    }
    @Singleton
    @Provides
    fun providesCategoryDAO(@ApplicationContext appContext: Context) = LibraryDatabase.getInstance(appContext).getCategoryDAO()

    @Singleton
    @Provides
    fun providesBookDAO(@ApplicationContext appContext: Context) = LibraryDatabase.getInstance(appContext).getBookDAO()

    @Singleton
    @Provides
    fun providesBOBook(@ApplicationContext appContext: Context,daoBook: BookDAO) = BOBook(daoBook)

    @Singleton
    @Provides
    fun providesBOCategory(@ApplicationContext appContext: Context,daoCategory: CategoryDAO, boBook :BOBook) = BOCategory(daoCategory,boBook)

    @Singleton
    @Provides
    fun providesBookClickHandlers(@ApplicationContext appContext: Context) = BookClickHandlers()

    @Singleton
    @Provides
    fun providesMyMQTTHandler(@ApplicationContext appContext: Context) = MyMQTTHandler()

    @Singleton
    @Provides
    fun providesDBPopulator(@ApplicationContext appContext: Context, boCategory :BOCategory, boBook :BOBook) = DBPopulator(boCategory,boBook)
}