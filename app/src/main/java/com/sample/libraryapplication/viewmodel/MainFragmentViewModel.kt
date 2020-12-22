package com.sample.libraryapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.bo.BOCategory
import com.sample.libraryapplication.database.DBPopulator
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity
import javax.inject.Inject

class MainFragmentViewModel(): BaseViewModel() {
    companion object{
        private const val TAG = "BookListViewModel"
    }

    @Inject
    lateinit var boCategory: BOCategory
    @Inject
    lateinit var dbPopulator: DBPopulator
//    var categories: MutableLiveData<MutableList<CategoryEntity>>? = null
    public var isLoading = MutableLiveData<Boolean>()


    override fun registerWithComponent() {
        LibraryApplication.instance.libraryComponent.inject( this)
    }

    fun getBooksListSelectedCategory(categoryID: Long) : LiveData<List<BookEntity>> {
        var selectedCategory = boCategory.categories.value?.filter { it.id == categoryID }?.first() as CategoryEntity

        with(boCategory){
            setEntity(selectedCategory)
            find(selectedCategory.id!!)
            refreshBooks()
        }
        return boCategory.books
      }

    fun deleteBook(book: BookEntity) {
        boCategory.deleteBook(book)
    }

 }