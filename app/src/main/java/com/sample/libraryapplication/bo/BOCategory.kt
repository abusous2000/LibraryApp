package com.sample.libraryapplication.bo

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.database.dao.BaseDAO
import com.sample.libraryapplication.database.dao.CategoryDAO
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity
import com.sample.libraryapplication.view.MainActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BOCategory @Inject constructor(val categoryDAO: CategoryDAO, val boBook: BOBook): BOAbstract<CategoryEntity>(){
    lateinit var category: LiveData<CategoryEntity>
    lateinit var books: LiveData<List<BookEntity>>
    var categories = MutableLiveData(mutableListOf<CategoryEntity>())
    //This is a hack, for some reason observer of categories are not notified only once
    public var categoryListUpdated = MutableLiveData<Boolean>()
    override fun getDAO(): BaseDAO<CategoryEntity> {
        listOf<CategoryEntity>()
        return categoryDAO
    }

    override fun getEntity(): LiveData<CategoryEntity> {
        return category
    }
    override fun setEntity(entity: CategoryEntity):  BOCategory {
        if( entity.id != null )
            this.id = entity.id!!
        category= MutableLiveData(entity)
        return this
    }
    fun booksInitalized(): Boolean{
        return this::books.isInitialized
    }
    override fun find(id: Long): BOCategory {
        this.id = id
        category = categoryDAO.findCategory(id)!!

        return this
    }
    fun findAll(): LiveData<MutableList<CategoryEntity>> {
        return categoryDAO.findAll()
    }
    override fun delete() {
       super.delete()
    }
    fun removeObservers(lifecycleOwner: LifecycleOwner){
        if ( booksInitalized() && books.hasObservers())
            books.removeObservers(lifecycleOwner)
        else
            Log.d(MainActivity.TAG, "removeObserver: no observers for books")
    }
    fun refreshBooks(){
        if ( category.value == null) {
            if ( this.id == -1L )
                throw Exception("invalid state")
            else
               books = boBook.findByCategory(this.id)
        }
        else {
            books = boBook.findByCategory(category.value?.id!!)
        }
    }
    fun addBook(book: BookEntity) {
        boBook.setEntity(book).insert()
    }
    fun deleteBook(book: BookEntity) {
        boBook.setEntity(book).delete()
    }
    fun updateBook(book: BookEntity) {
        boBook.setEntity(book).update()
    }
}