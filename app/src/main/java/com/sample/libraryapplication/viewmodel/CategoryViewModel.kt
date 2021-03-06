package com.sample.libraryapplication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.bo.BOCategory
import com.sample.libraryapplication.database.entity.CategoryEntity
import javax.inject.Inject

class CategoryViewModel: BaseViewModel()  {
    private val TAG = "CategoryViewModel"
    @Inject
    lateinit var boCategory: BOCategory

    val isCategoryNameEmpty = MutableLiveData<Boolean>()
    val isCategoryPriceEmpty = MutableLiveData<Boolean>()
    val shouldFinishActivity = MutableLiveData<Boolean>()
    var isUpdateCategory = false
    var selectedCategoryId: Long? = null
    var selectedCategory: CategoryEntity? = null
    var categoryName: String? = null
    var categoryDesc: String? = null

    override fun registerWithComponent() {
        LibraryApplication.instance.libraryComponent.inject( this)
        selectedCategoryId?.let { boCategory.find(it) }
    }

    private fun addNewCategory(category: CategoryEntity) {
        with(boCategory){
            setEntity(category)
            insert()
            categories.value?.add(category)
            categoryListUpdated.postValue(true)
        }
    }

    private fun updateCategory(category: CategoryEntity) {
        with(boCategory){
            setEntity(category)
            update()
            categoryListUpdated.postValue(true)
        }
    }
    fun clear(){
        isCategoryNameEmpty.value = false
        isCategoryPriceEmpty.value = false
        shouldFinishActivity.value = false
    }
    fun setCategoryName(charSequence: CharSequence) {
        categoryName = charSequence.toString()
//        Log.d(TAG, "setCategoryName: $categoryName")
    }

    fun setCategoryDesc(charSequence: CharSequence) {
        categoryDesc = charSequence.toString()
//        Log.d(TAG, "setCategoryPrice: $categoryPrice")
    }

    fun saveCategory() {
        isCategoryNameEmpty.value = categoryName.isNullOrEmpty()
        isCategoryPriceEmpty.value = categoryDesc.isNullOrEmpty()

        selectedCategoryId?.let { boCategory.find(it) }
        if (!categoryName.isNullOrEmpty() && !categoryDesc.isNullOrEmpty()) {
            if (isUpdateCategory) {
                selectedCategory?.let { category ->
                    category.categoryName = categoryName!!
                    category.categoryDesc = categoryDesc!!
                    updateCategory(category)
                }
            } else {
                val newCategory = CategoryEntity()
                newCategory.categoryName = categoryName!!
                newCategory.categoryDesc = categoryDesc!!
                addNewCategory(newCategory)
            }
            shouldFinishActivity.value = true
        } else
            shouldFinishActivity.value = false
    }
 }