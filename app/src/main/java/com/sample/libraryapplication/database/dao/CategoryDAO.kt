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
    override fun getTableName(): String{
        return TABLE_NAME;
    }

    @Query("SELECT * FROM categories WHERE id == :id")
    fun findCategory(id: Long) : LiveData<CategoryEntity>?
}