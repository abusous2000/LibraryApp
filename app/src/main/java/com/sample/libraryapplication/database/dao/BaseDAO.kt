package com.sample.libraryapplication.database.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.sample.libraryapplication.database.entity.BookEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface BaseDAO<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: T?) : Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(entity: List<T>)
    @Update
    fun update(entity: T?)

    @Delete
    fun delete(entity: T?)

    @RawQuery
    fun findById(query: SupportSQLiteQuery): T?

    fun getTableName(): String
    //TODO: For some reason, if you use this..an exception will be thrown unless you run the DB from the main thread; which is risky
    open fun find(id: Long): LiveData<T>?{
        var query = SimpleSQLiteQuery("SELECT * FROM " + getTableName() + " WHERE id = $id LIMIT 1")

        return MutableLiveData(findById(query));
    }
    @RawQuery
    fun findAll2(query: SupportSQLiteQuery):  MutableList<T>

    open fun findAll(): MutableLiveData<MutableList<T>>{
        var lv = MutableLiveData(mutableListOf<T>())
        CoroutineScope(Dispatchers.IO).launch {
            var list= findAll2(SimpleSQLiteQuery("SELECT * FROM " + getTableName()))

            lv.postValue(list)
        }
        return lv
    }
 }