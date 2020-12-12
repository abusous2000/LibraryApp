package com.sample.libraryapplication.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity

@Dao
interface BookDAO: BaseDAO<BookEntity> {
    companion object{
        const val TABLE_NAME = "books"
    }
    @Query("SELECT * FROM books WHERE id == :id")
    fun find(id: Long) : BookEntity?
    @Query("SELECT * FROM $TABLE_NAME")
    fun findAll() : LiveData<List<BookEntity>>
    @Query("SELECT * FROM $TABLE_NAME WHERE book_category_id == :categoryID")
    fun findAllByCategory(categoryID: Long) : LiveData<List<BookEntity>>
}