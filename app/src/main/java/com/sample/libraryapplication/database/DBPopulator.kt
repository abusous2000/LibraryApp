package com.sample.libraryapplication.database

import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.R
import com.sample.libraryapplication.bo.BOBook
import com.sample.libraryapplication.bo.BOCategory
import com.sample.libraryapplication.dagger.module.LibraryAppModule
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity
import com.sample.libraryapplication.service.BooksRestfulService
import com.sample.libraryapplication.service.MyPrefsRespository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DBPopulator @Inject constructor(val boCategory: BOCategory, val boBook: BOBook, val booksRestfulService :BooksRestfulService) {
    companion object{
        val defaultGithubJsonDbAccount = "abusous2000"
        val GITHUB_JSON_DB_ACCOUNT = "GithubJsonDbAccount"
        val bookResources = intArrayOf(R.drawable.connect, R.drawable.ic_drawer, R.drawable.table,R.drawable.ic_launcher_background,R.drawable.ic_baseline_dehaze_24)
    }
    val TAG = "DBPopulator"
    init {

    }
    val myPrefs: MyPrefsRespository by lazy {
        MyPrefsRespository(LibraryApplication.instance.applicationContext)
    }
    var bookList = arrayListOf<BookEntity>()
    var categoryList = arrayListOf<CategoryEntity>()
    val dbPopulated =  MutableLiveData<Boolean>()
    var categoriesLoaded = false
    var booksLoaded = false


    fun doesDbExist(context: Context): Boolean {
        val dbFile: File = context.getDatabasePath(LibraryAppModule.DB_NAME)
        return dbFile.exists()
    }
    fun populateDB() {
        dbPopulated.postValue(true)
  //        val gson = GsonBuilder().serializeNulls().create()
//        val githubJsonDbAccount = myPrefs.getString(GITHUB_JSON_DB_ACCOUNT, defaultGithubJsonDbAccount)
//        val loggingInterceptor = HttpLoggingInterceptor()
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//        val okHttpClient = OkHttpClient.Builder()
//                .addInterceptor(loggingInterceptor)
//                .build()
//        val retrofit = Retrofit.Builder()
//                .baseUrl("https://my-json-server.typicode.com/$githubJsonDbAccount/demo/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .callbackExecutor(Executors.newSingleThreadExecutor())
//                .client(okHttpClient)
//                .build()

//        val booksRestfulService = retrofit.create(BooksRestfulService::class.java)
        val call: Call<List<CategoryEntity>> = booksRestfulService.getCategories()
        call.enqueue(object : Callback<List<CategoryEntity>> {
            override fun onResponse(call: Call<List<CategoryEntity>>, response: Response<List<CategoryEntity>>
            ) {
                if ( !response.isSuccessful() ){
                    Log.d("RoomDatabaseModule","Failed to get Books in JSON")
                    return;
                }
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
                    for (category in categories) {
                        Log.d("RoomDatabaseModule","Parsed category:${category.toString()}")
                        categoryList.add(category)
                    }
                    boCategory.categoryDAO.insertAll(categoryList)
                    categoriesLoaded = true;
                    Log.d("RoomDatabaseModule","Insert into db:${categoryList.size} categories")
                }
           }

            override fun onFailure(call: Call<List<CategoryEntity>>, t: Throwable) {
                Log.e("RoomDatabaseModule", t.message!!)
            }
        })
        Thread.sleep(200L)
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
                if ( books != null) {
                    for (book in books) {
                        Log.d("RoomDatabaseModule","Parsed book:${book.toString()}")
                        book.resourceId=bookResources[book.resourceId]
                        bookList.add(book)
                    }
                    Log.d("RoomDatabaseModule","parsed into db:${bookList.size} books")
                    if ( categoriesLoaded) {
                        boBook.bookDAO.insertAll(books)
                        booksLoaded = true
                        dbPopulated.postValue(true)
                        Log.d("RoomDatabaseModule","Insert into db:${bookList.size} books")
                    }
                    else
                        Log.d(TAG, "onResponse: No books were cretated")
                }
                else
                    Log.d(TAG, "onResponse: No books were cretated")
           }

            override fun onFailure(call: Call<List<BookEntity>>, t: Throwable) {
                Log.e("RoomDatabaseModule", t.message!!)
            }
        })
    }
}