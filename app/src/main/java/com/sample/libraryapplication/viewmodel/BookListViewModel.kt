package com.sample.libraryapplication.viewmodel

import android.content.Intent
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.dagger.module.RoomDatabaseModule
import com.sample.libraryapplication.database.LibraryDatabase
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity
import com.sample.libraryapplication.repository.BookRepository
import com.sample.libraryapplication.repository.CategoryRepository
import com.sample.libraryapplication.view.BookActivity
import javax.inject.Inject

class BookListViewModel: BaseViewModel() {
    @Inject
    lateinit var bookRepository: BookRepository

    public var allCategories: LiveData<List<CategoryEntity>> = libraryDatabase.getCategoryDAO().getAllCategories()
    public var isLoading = MutableLiveData<Boolean>()

    override fun registerWithComponent() {
        LibraryApplication.instance.libraryComponent.inject( this)
    }

    fun getBooksListSelectedCategory(categoryID: Long) : LiveData<List<BookEntity>> {
        var tmp = bookRepository.getBooks(categoryID)
        var tmp2 = tmp.value
        return tmp;
    }

    fun deleteBook(book: BookEntity) {
        bookRepository.deleteBook(book)
    }
 }