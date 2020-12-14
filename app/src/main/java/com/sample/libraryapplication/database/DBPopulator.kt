package com.sample.libraryapplication.database

import android.os.Looper
import android.util.Log
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.R
import com.sample.libraryapplication.bo.BOBook
import com.sample.libraryapplication.bo.BOCategory
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity
import com.sample.libraryapplication.utils.BooksRestfulService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DBPopulator @Inject constructor() {
    init {
        LibraryApplication.instance.libraryComponent.inject(this)
    }

    @Inject
    lateinit var boCategory: BOCategory
    @Inject
    lateinit var boBook: BOBook
    var bookList = arrayListOf<BookEntity>()
    var categoryList = arrayListOf<CategoryEntity>()
    var dbIsBeingPopulated = false
    var categoriesLoaded = false
    var booksLoaded = false
    fun populateDB() {
        dbIsBeingPopulated = true;
        val bookResources = intArrayOf(R.drawable.ic_baseline_adb_24, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background)
  //        val gson = GsonBuilder().serializeNulls().create()
        LibraryApplication.instance.libraryComponent.inject(this)

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        val retrofit = Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/abusous2000/demo/")
                .addConverterFactory(GsonConverterFactory.create())
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .client(okHttpClient)
                .build()

        val booksRestfulService = retrofit.create(BooksRestfulService::class.java)
        val call: Call<List<CategoryEntity>> = booksRestfulService.getCategories()
        call.enqueue(object : Callback<List<CategoryEntity>> {
            override fun onResponse(call: Call<List<CategoryEntity>>, response: Response<List<CategoryEntity>>
            ) {
                if ( Looper.getMainLooper().thread.id == Thread.currentThread().id)
                    Log.d("RoomDatabaseModule","++++++++++++++++Main thread")
                else
                    Log.d("RoomDatabaseModule","------------------NOT Main thread")

                if ( !response.isSuccessful() ){
                    Log.d("RoomDatabaseModule","Failed to get Catgeories in JSON")
                    return;
                }
                val categories: List<CategoryEntity>? = response.body()// as List<CategoryEntity>
                if (categories != null) {
//                    boCategory.categoryDAO.insertAll(categories)
                    for (category in categories) {
                        Log.d("RoomDatabaseModule","Parsed category:${category.toString()}")
                        categoryList.add(category)
//                        boCategory.setEntity(category).insert()

                    }
                    boCategory.categoryDAO.insertAll(categoryList)
                    boCategory.categories = boCategory.findAll()
                    categoriesLoaded = true;
                }

            }

            override fun onFailure(call: Call<List<CategoryEntity>>, t: Throwable) {
                Log.e("RoomDatabaseModule", t.message!!)
            }
        })

        var call2: Call<List<BookEntity>> = booksRestfulService.getBooks()
        call2.enqueue(object : Callback<List<BookEntity>> {
            override fun onResponse(call: Call<List<BookEntity>>, response: Response<List<BookEntity>>
            ) {
                if ( !response.isSuccessful() ){
                    Log.d("RoomDatabaseModule","Failed to get Books in JSON")
                    return;
                }
                var i = 0
                //Poll unitil categroies creates
                while ( categoriesLoaded == false && i < 100 ) {
                    Thread.sleep(50L)
                    ++i
                }
                Log.d("RoomDatabaseModule", "Creating books where i=$i and categoriesCreated=$categoriesLoaded")
                val books: List<BookEntity>? = response.body()// as List<BookEntity>
                if ( categoriesLoaded && books != null) {
                    for (book in books) {
                        Log.d("RoomDatabaseModule","Parsed book:${book.toString()}")
                        book.resourceId=bookResources[book.resourceId]
                        bookList.add(book)
//                        boBook.setEntity(book).insert()
                    }

                    boBook.bookDAO.insertAll(books)
                    booksLoaded = true
                }

            }

            override fun onFailure(call: Call<List<BookEntity>>, t: Throwable) {
                Log.e("RoomDatabaseModule", t.message!!)
            }
        })
    }
}