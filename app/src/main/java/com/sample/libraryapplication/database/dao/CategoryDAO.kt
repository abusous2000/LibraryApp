package com.sample.libraryapplication.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity

@Dao
interface CategoryDAO: BaseDAO<CategoryEntity> {
    companion object{
        const val TABLE_NAME = "categories"
    }
    @Query("SELECT * FROM $TABLE_NAME ")
    fun findAll() : LiveData<List<CategoryEntity>>

    @Query("SELECT * FROM $TABLE_NAME WHERE id == :id")
    fun find(id: Long) : CategoryEntity?
}