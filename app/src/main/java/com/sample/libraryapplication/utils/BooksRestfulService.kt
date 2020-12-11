package com.sample.libraryapplication.utils

import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity
import retrofit2.Call
import retrofit2.http.GET

interface BooksRestfulService {
    @GET("books")
    fun getBooks(): Call<List<BookEntity>>
    @GET("categories")
    fun getCategories(): Call<List<CategoryEntity>>
}