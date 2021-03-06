package com.sample.libraryapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.bo.BOCategory
import com.sample.libraryapplication.database.DBPopulator
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity
import javax.inject.Inject

class CategoryListFragmentViewModel(): BaseViewModel() {
    companion object{
        private const val TAG = "CategoryListFragmentViewModel"
    }

    @Inject
    lateinit var boCategory: BOCategory
    var isLoading = MutableLiveData<Boolean>()
    override fun registerWithComponent() {
        LibraryApplication.instance.libraryComponent.inject( this)
    }

    fun deleteCategory(category: CategoryEntity) {
        with(boCategory){
            Log.d(TAG, "deleteCategory with Id: ${category.id}")
            setEntity(category)
            delete()
            categories.value?.remove(category)
            categoryListUpdated.postValue(true)
        }
    }
 }