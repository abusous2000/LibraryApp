package com.sample.libraryapplication.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sample.libraryapplication.database.entity.CategoryEntity

interface BaseDAO<T> {
//    companion object{
//        const val TABLE_NAME = "BaseDAO"
//    }
//    @Query("SELECT * FROM $TABLE_NAME ")
//    fun findAll() : LiveData<List<T>>

    @Insert
    fun insert(entity: T) : Long

    @Update
    fun update(entity: T)

    @Delete
    fun delete(entity: T?)
 }