package com.sample.libraryapplication.bo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sample.libraryapplication.database.dao.BaseDAO
import com.sample.libraryapplication.database.entity.BaseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BOAbstract<T: BaseEntity> {
    companion object {
        private const val TAG = "BOAbstract"
    }
    protected var id: Long = -1
    abstract fun getDAO(): BaseDAO<T>
    abstract fun getEntity(): LiveData<T>?
    abstract fun find(id:Long): BOAbstract<T>
    abstract fun setEntity(entity: T): BOAbstract<T>
    open fun insert() {
        if ( getEntity() != null ) {
            Log.d(TAG,"Inserting:" + getEntity()?.value?.id)
            CoroutineScope(Dispatchers.IO).launch {
                     getDAO().insert(getEntity()?.value)
               }
        }
    }

    open fun update() {
        if ( getEntity() != null ) {
            Log.d("BOAbstract","Updating:" + getEntity()?.value?.id)
            CoroutineScope(Dispatchers.IO).launch {
                getDAO().update(getEntity()?.value)
            }
        }
    }

    open fun delete() {
        if ( getEntity() != null ) {
            Log.d("BOAbstract","deleting:"+ getEntity()?.value?.id)
            CoroutineScope(Dispatchers.IO).launch {
                getDAO().delete(getEntity()?.value)
            }
        }
    }
}