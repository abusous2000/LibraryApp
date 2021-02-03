package com.sample.libraryapplication.dagger.module

import android.content.Context
import com.sample.libraryapplication.BuildConfig
import com.sample.libraryapplication.bo.BOBook
import com.sample.libraryapplication.bo.BOCategory
import com.sample.libraryapplication.database.DBPopulator
import com.sample.libraryapplication.database.LibraryRoomDB
import com.sample.libraryapplication.database.dao.BookDAO
import com.sample.libraryapplication.database.dao.CategoryDAO
import com.sample.libraryapplication.service.BooksRestfulService
import com.sample.libraryapplication.service.MyMQTTHandler
import com.sample.libraryapplication.view.BookClickHandlers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
class LibraryAppModule() {
    companion object {
        private const val EDUCATIONAL_BOOKS_CATEGORY_ID = 1L
        private const val NOVELS_CATEGORY_ID = 2L
        private const val OTHER_BOOKS_CATEGORY_ID = 3L
        const val DB_NAME = "library_database"
    }
    @Singleton
    @Provides
    fun providesCategoryDAO(@ApplicationContext appContext: Context) = LibraryRoomDB.getInstance(appContext).getCategoryDAO()

    @Singleton
    @Provides
    fun providesBookDAO(@ApplicationContext appContext: Context) = LibraryRoomDB.getInstance(appContext).getBookDAO()

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

    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
    } else OkHttpClient
            .Builder()
            .build()

    @Singleton
    @Provides
    fun providesLibraryRetrofit(okHttpClient: OkHttpClient,): Retrofit {
        val githubJsonDbAccount = BuildConfig.githubJsonDbAccount
        val retrofit = Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/$githubJsonDbAccount/demo/")
                .addConverterFactory(GsonConverterFactory.create())
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .client(okHttpClient)
                .build()

        return retrofit;
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): BooksRestfulService = retrofit.create(BooksRestfulService::class.java)
    @Singleton
    @Provides
    fun providesDBPopulator(@ApplicationContext appContext: Context, boCategory :BOCategory, boBook :BOBook, booksRestfulService :BooksRestfulService) = DBPopulator(boCategory,boBook,booksRestfulService)
}