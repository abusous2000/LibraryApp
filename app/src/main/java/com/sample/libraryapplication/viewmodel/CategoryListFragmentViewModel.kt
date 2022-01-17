package com.sample.libraryapplication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.sample.libraryapplication.bo.BOCategory
import com.sample.libraryapplication.database.entity.CategoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryListFragmentViewModel @Inject constructor(val boCategory: BOCategory): BaseViewModel() {
    companion object{
        private const val TAG = "CategoryListFragmentViewModel"
    }

    var isLoading = MutableLiveData<Boolean>()
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