package com.sample.libraryapplication.viewmodel

import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.bo.BOCategory
import com.sample.libraryapplication.database.DBPopulator
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity
import com.sample.libraryapplication.view.ActivityWeakRef
import com.sample.libraryapplication.view.BookListActivity
import javax.inject.Inject

class BookListViewModel: BaseViewModel() {
    companion object{
        private const val TAG = "BookListViewModel"
    }

    @Inject
    lateinit var boCategory: BOCategory
    @Inject
    lateinit var dbPopulator: DBPopulator
    lateinit var allCategories: LiveData<List<CategoryEntity>>
    public var isLoading = MutableLiveData<Boolean>()

    override fun registerWithComponent() {
        LibraryApplication.instance.libraryComponent.inject( this)
        allCategories = boCategory.findAll()
        if ( allCategories.value?.size == 0 ){
//            Handler(Looper.getMainLooper()).postDelayed({
//                AsyncTask.execute({
//                    LibraryApplication.roomDatabaseModule.libraryDatabase.runInTransaction({
//                        dbPopulator.populateDB()
//                    })
//                })
//            },200)

            Handler(Looper.getMainLooper()).postDelayed({
                var mutableCategories = allCategories as MutableLiveData<List<CategoryEntity>>
                var freshCategoryList = boCategory.categories
//                var freshCategoryList = boCategory.findAll()

                if ( freshCategoryList !=null && freshCategoryList?.value?.size!! > 0  )
                    mutableCategories.postValue(freshCategoryList?.value)
            },1500)
          }
    }

    fun getBooksListSelectedCategory(categoryID: Long) : LiveData<List<BookEntity>> {
        var selectedCategory = allCategories.value?.filter { it.id == categoryID }?.first() as CategoryEntity
        boCategory.find(selectedCategory.id!!).refreshBooks()
        return boCategory.books
    }

    fun deleteBook(book: BookEntity) {
        boCategory.deleteBook(book)
    }

 }