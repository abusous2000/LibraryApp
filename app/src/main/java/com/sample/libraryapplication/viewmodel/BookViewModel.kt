package com.sample.libraryapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.bo.BOCategory
import com.sample.libraryapplication.database.entity.BookEntity
import javax.inject.Inject

class BookViewModel: BaseViewModel()  {
    @Inject
    lateinit var boCategory: BOCategory


    val isBookNameEmpty = MutableLiveData<Boolean>()
    val isBookPriceEmpty = MutableLiveData<Boolean>()
    val shouldFinishActivity = MutableLiveData<Boolean>()
    var isUpdateBook = false
    var selectedCategoryId: Long? = null
    var selectedBook: BookEntity? = null
    var bookName: String? = null
    var bookPrice: String? = null

    override fun registerWithComponent() {
        LibraryApplication.instance.libraryComponent.inject( this)
        selectedCategoryId?.let { boCategory.find(it) }
    }

    private fun addNewBook(book: BookEntity) {
        boCategory.addBook(book)
    }

    private fun updateBook(book: BookEntity) {
        boCategory.updateBook(book)
    }

    fun setBookName(charSequence: CharSequence) {
        bookName = charSequence.toString()
    }

    fun setBookPrice(charSequence: CharSequence) {
        bookPrice = charSequence.toString()
    }

    fun saveBook() {
        isBookNameEmpty.value = bookName.isNullOrEmpty()
        isBookPriceEmpty.value = bookPrice.isNullOrEmpty()

        selectedCategoryId?.let { boCategory.find(it) }
        if (!bookName.isNullOrEmpty() && !bookPrice.isNullOrEmpty()) {
            if (isUpdateBook) {
                selectedBook?.let { book ->
                    book.bookName = bookName
                    book.bookUnitPrice = bookPrice?.toDouble()
                    updateBook(book)
                }
            } else {
                val newBook = BookEntity()
                newBook.bookName = bookName
                newBook.bookUnitPrice = bookPrice?.toDouble()
                newBook.bookCategoryID = selectedCategoryId
                addNewBook(newBook)
            }
            shouldFinishActivity.value = true
        } else
            shouldFinishActivity.value = false
    }
 }