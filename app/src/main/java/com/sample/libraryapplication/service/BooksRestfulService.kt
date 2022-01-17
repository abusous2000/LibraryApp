package com.sample.libraryapplication.service

import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity
import retrofit2.Call
import retrofit2.http.GET

interface BooksRestfulService {
    @GET("books")
    //https://my-json-server.typicode.com/abusous2000/demo/books
    fun getBooks(): Call<List<BookEntity>>
    @GET("categories")
    //https://my-json-server.typicode.com/abusous2000/demo/categories
    fun getCategories(): Call<List<CategoryEntity>>
}