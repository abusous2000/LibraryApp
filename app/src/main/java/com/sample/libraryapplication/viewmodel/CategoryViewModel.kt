package com.sample.libraryapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import com.sample.libraryapplication.bo.BOCategory
import com.sample.libraryapplication.database.entity.CategoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(val boCategory: BOCategory): BaseViewModel()  {
    private val TAG = "CategoryViewModel"

    val isCategoryNameEmpty = MutableLiveData<Boolean>()
    val isCategoryPriceEmpty = MutableLiveData<Boolean>()
    val shouldFinishActivity = MutableLiveData<Boolean>()
    var isUpdateCategory = false
    var selectedCategoryId: Long? = null
    var selectedCategory: CategoryEntity? = null
    var categoryName: String? = null
    var categoryDesc: String? = null

    init{
        registerWithComponent()
    }
    fun registerWithComponent() {
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