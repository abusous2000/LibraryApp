package com.sample.libraryapplication.bo

import android.util.Log
import com.sample.libraryapplication.database.dao.BaseDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BOAbstract<T> {
    abstract fun getDAO(): BaseDAO<T>
    abstract fun getEntity(): T
    open fun insert() {
        if ( getEntity() != null ) {
            Log.d("BOAbstract","Inserting")
            CoroutineScope(Dispatchers.IO).launch {
                getDAO().insert(getEntity())
            }
        }
    }

    open fun update() {
        if ( getEntity() != null ) {
            Log.d("BOAbstract","Updating")
            CoroutineScope(Dispatchers.IO).launch {
                getDAO().update(getEntity())
            }
        }
    }

    open fun delete() {
        if ( getEntity() != null ) {
            Log.d("BOAbstract","deleting")
            CoroutineScope(Dispatchers.IO).launch {
                getDAO().delete(getEntity())
            }
        }
    }
}