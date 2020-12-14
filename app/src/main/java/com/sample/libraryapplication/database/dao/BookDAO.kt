package com.sample.libraryapplication.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity

@Dao
interface BookDAO: BaseDAO<BookEntity> {
    companion object{
        const val TABLE_NAME = "books"
    }
    override fun getTableName(): String{
        return "books";
    }
    @Query("SELECT * FROM $TABLE_NAME WHERE book_category_id == :categoryID")
    fun findAllByCategory(categoryID: Long) : LiveData<List<BookEntity>>
    @Query("SELECT * FROM books WHERE id == :id")
    fun findBook(id: Long) : LiveData<BookEntity>?

}