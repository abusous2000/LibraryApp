package com.sample.libraryapplication.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sample.libraryapplication.bo.BOCategory
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity

class BookListFragmentViewModel @ViewModelInject constructor(val boCategory: BOCategory): BaseViewModel() {
    companion object{
        private const val TAG = "BookListFragmentViewModel"
        public var selectedCategory: CategoryEntity? = null
    }
    var isLoading = MutableLiveData<Boolean>()
    fun getBooksListSelectedCategory(categoryID: Long) : LiveData<List<BookEntity>> {
        selectedCategory = boCategory.categories.value?.filter { it.id == categoryID }?.first() as CategoryEntity

        with(boCategory){
            setEntity(selectedCategory!!)
            find(selectedCategory!!.id!!)
            refreshBooks()
        }
        return boCategory.books
      }

    fun deleteBook(book: BookEntity) {
        boCategory.deleteBook(book)
    }
 }