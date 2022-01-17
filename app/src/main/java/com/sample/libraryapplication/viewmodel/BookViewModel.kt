package com.sample.libraryapplication.viewmodel

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.sample.libraryapplication.bo.BOCategory
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.utils.ActivityWeakMapRef
import com.sample.libraryapplication.utils.showColoredToast
import com.sample.libraryapplication.view.MainActivity
import com.sample.libraryapplication.view.fragment.BookListFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(val boCategory: BOCategory): BaseViewModel()  {
    private val TAG = "BookViewModel"

    val isBookNameEmpty = MutableLiveData<Boolean>()
    val isBookPriceEmpty = MutableLiveData<Boolean>()
    val shouldFinishActivity = MutableLiveData<Boolean>()
    var isUpdateBook = false
    var selectedCategoryId: Long? = null
    var selectedBook: BookEntity? = null
    var bookName: String? = null
    var bookPrice: String? = null

    init{
        registerWithComponent()
    }
    fun registerWithComponent() {

        selectedCategoryId?.let { boCategory.find(it) }
    }

    private fun addNewBook(book: BookEntity) {
        boCategory.addBook(book)
    }

    private fun updateBook(book: BookEntity) {
        boCategory.updateBook(book)
    }
    fun clear(){
        isBookNameEmpty.value = false
        isBookPriceEmpty.value = false
        shouldFinishActivity.value = false
    }
    fun setBookName(charSequence: CharSequence) {
        bookName = charSequence.toString()
//        Log.d(TAG, "setBookName: $bookName")
    }

    fun setBookPrice(charSequence: CharSequence) {
        bookPrice = charSequence.toString()
//        Log.d(TAG, "setBookPrice: $bookPrice")
    }

    fun saveBook() {
        isBookNameEmpty.value = bookName.isNullOrEmpty()
        isBookPriceEmpty.value = bookPrice.isNullOrEmpty()

        selectedCategoryId?.let { boCategory.find(it) }
        if (!bookName.isNullOrEmpty() && !bookPrice.isNullOrEmpty()) {
            var info = "Book updated"
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
                info = "Book added"
            }
            showColoredToast(info)
            Handler(Looper.getMainLooper()).postDelayed({
                val mainActivity = ActivityWeakMapRef.get(MainActivity.TAG) as MainActivity
                mainActivity.startActivity(Intent(mainActivity,MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                mainActivity.finish();
                Log.d(BookListFragment.TAG, "Re-Routing to MainActivity")
            }, 600)

            shouldFinishActivity.value = true
        } else
            shouldFinishActivity.value = false
    }
 }