package com.sample.libraryapplication.dagger.module

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sample.libraryapplication.R
import com.sample.libraryapplication.database.LibraryDatabase
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity
import com.sample.libraryapplication.utils.BooksRestfulService
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class RoomDatabaseModule(application: Application) {

    private var libraryApplication = application
    private lateinit var libraryDatabase: LibraryDatabase

    companion object {
        private const val EDUCATIONAL_BOOKS_CATEGORY_ID = 1L
        private const val NOVELS_CATEGORY_ID = 2L
        private const val OTHER_BOOKS_CATEGORY_ID = 3L
    }

    private val databaseCallback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.d("RoomDatabaseModule", "onCreate")
            CoroutineScope(Dispatchers.IO).launch {
                addSampleBooksToDatabase()
            }
        }
    }
    private var categoriesCreated = false
    private fun addSampleBooksToDatabase() {
        val bookResources = intArrayOf(R.drawable.ic_baseline_adb_24,R.drawable.ic_launcher_foreground,R.drawable.ic_launcher_background)
        /*
        val category1 = CategoryEntity(EDUCATIONAL_BOOKS_CATEGORY_ID, "Educational Books", "Educational Books Desc")
        val category2 = CategoryEntity(NOVELS_CATEGORY_ID, "Novels", "Novels Desc")
        val category3 = CategoryEntity(OTHER_BOOKS_CATEGORY_ID, "Other Books", "Non Categorized Books")


        val categoryDAO = libraryDatabase.getCategoryDAO()
        categoryDAO.addCategory(category1)
        categoryDAO.addCategory(category2)
        categoryDAO.addCategory(category3)

          val book1 = BookEntity(1, "Java Programming Book", 10.50, EDUCATIONAL_BOOKS_CATEGORY_ID, bookResources[0])
          val book2 = BookEntity(2, "Mathematics", 19.10, EDUCATIONAL_BOOKS_CATEGORY_ID, bookResources[1])
          val book3 = BookEntity(3, "Adventures of Joe Finn", 25.30, NOVELS_CATEGORY_ID, R.drawable.ic_launcher_background)
          val book4 = BookEntity(4, "The Hound the New York", 5.30, NOVELS_CATEGORY_ID,R.drawable.ic_baseline_adb_24)
          val book5 = BookEntity(5, "Astrology", 56.99, OTHER_BOOKS_CATEGORY_ID, R.drawable.ic_launcher_foreground)
          val book6 = BookEntity(6, "Arc of Witches", 34.99, OTHER_BOOKS_CATEGORY_ID, R.drawable.ic_baseline_adb_24)
          val book7 = BookEntity(7, "Can I Run?", 99.99, NOVELS_CATEGORY_ID, R.drawable.ic_launcher_foreground)
          val book8 = BookEntity(8, "Basic of Physics", 10.50, EDUCATIONAL_BOOKS_CATEGORY_ID, R.drawable.ic_baseline_adb_24)



          bookDAO.addBook(book1)
          bookDAO.addBook(book2)
          bookDAO.addBook(book3)
          bookDAO.addBook(book4)
          bookDAO.addBook(book5)
          bookDAO.addBook(book6)
          bookDAO.addBook(book7)
          bookDAO.addBook(book8)
        */
//        val gson = GsonBuilder().serializeNulls().create()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://my-json-server.typicode.com/abusous2000/demo/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val booksRestfulService = retrofit.create(BooksRestfulService::class.java)
        val call: Call<List<CategoryEntity>> = booksRestfulService.getCategories()
        val categoryDAO = libraryDatabase.getCategoryDAO()
        call.enqueue(object : Callback<List<CategoryEntity>> {
            override fun onResponse(call: Call<List<CategoryEntity>>,response: Response<List<CategoryEntity>>
            ) {
                val categories: List<CategoryEntity>? = response.body()// as List<CategoryEntity>
                if (categories != null) {
                    for (category in categories) {
                        Log.d("RoomDatabaseModule","inserting category:${category.toString()}")
                        categoryDAO.insert(CategoryEntity(category.id,category.categoryName,category.categoryDesc))
                    }
                }
                categoriesCreated = true;
            }

            override fun onFailure(call: Call<List<CategoryEntity>>, t: Throwable) {
                Log.e("RoomDatabaseModule", t.message!!)
            }
        })

        var call2: Call<List<BookEntity>> = booksRestfulService.getBooks()
        val bookDAO = libraryDatabase.getBookDAO()
        call2.enqueue(object : Callback<List<BookEntity>> {
            override fun onResponse(call: Call<List<BookEntity>>,response: Response<List<BookEntity>>
            ) {
                var i = 0
                //Poll unitil categroies creates
                while (categoriesCreated == false && i < 100 ) {
                    Thread.sleep(50L)
                    ++i
                }
                Log.d("RoomDatabaseModule", "Creating books where i=$i and categoriesCreated=$categoriesCreated")
                val books: List<BookEntity>? = response.body()// as List<BookEntity>
                if (categoriesCreated && books != null) {
                    for (book in books) {
                        Log.d("RoomDatabaseModule","inserting book:${book.toString()}")
                        bookDAO.insert(BookEntity(book.id,book.bookName,book.bookUnitPrice,book.bookCategoryID,bookResources[book.resourceId]))
                    }
                }
            }

            override fun onFailure(call: Call<List<BookEntity>>, t: Throwable) {
                Log.e("RoomDatabaseModule", t.message!!)
            }
        })

    }

    @Singleton
    @Provides
    fun providesRoomDatabase(): LibraryDatabase {
        libraryDatabase = Room.databaseBuilder(libraryApplication, LibraryDatabase::class.java, "library_database")
            .fallbackToDestructiveMigration()
            .addCallback(databaseCallback)
            .allowMainThreadQueries()
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