package com.sample.libraryapplication.bo

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.database.dao.BaseDAO
import com.sample.libraryapplication.database.dao.BookDAO
import com.sample.libraryapplication.database.dao.CategoryDAO
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity
import com.sample.libraryapplication.view.BookListActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BOCategory @Inject constructor(): BOAbstract<CategoryEntity>(){
    lateinit var category: LiveData<CategoryEntity>
    lateinit var books: LiveData<List<BookEntity>>
    var categories = MutableLiveData(mutableListOf<CategoryEntity>())
    public var categoryListUpdated = MutableLiveData<Boolean>()
    @Inject
    lateinit var boBook: BOBook
    @Inject
    lateinit var categoryDAO: CategoryDAO
    init{
        LibraryApplication.instance.libraryComponent.inject(this)
    }
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
       books.value?.forEach({
           boBook.setEntity(it).delete()
        })
       super.delete()
    }
    fun removeObserver(lifecycleOwner: LifecycleOwner){
        if ( booksInitalized() && books.hasObservers())
            books.removeObservers(lifecycleOwner)
        else
            Log.d(BookListActivity.TAG, "removeObserver: no observers for books")
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