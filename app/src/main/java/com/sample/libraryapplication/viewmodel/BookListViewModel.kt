package com.sample.libraryapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.bo.BOCategory
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity
import javax.inject.Inject

class BookListViewModel: BaseViewModel() {
    @Inject
    lateinit var boCategory: BOCategory

    lateinit var allCategories: LiveData<List<CategoryEntity>>
    public var isLoading = MutableLiveData<Boolean>()

    override fun registerWithComponent() {
        LibraryApplication.instance.libraryComponent.inject( this)
        allCategories = boCategory.findAll()
    }

    fun getBooksListSelectedCategory(categoryID: Long) : LiveData<List<BookEntity>> {
        return boCategory.find(categoryID).books
    }

    fun deleteBook(book: BookEntity) {
        boCategory.deleteBook(book)
    }

 }