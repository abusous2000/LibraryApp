package com.sample.libraryapplication.bo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.database.dao.BaseDAO
import com.sample.libraryapplication.database.dao.BookDAO
import com.sample.libraryapplication.database.dao.CategoryDAO
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BOCategory @Inject constructor(): BOAbstract<CategoryEntity>(){
     lateinit var category: LiveData<CategoryEntity>
    lateinit var books: LiveData<List<BookEntity>>
    var categories: MutableLiveData<List<CategoryEntity>>? = null
    @Inject
    lateinit var boBook: BOBook
    @Inject
    lateinit var categoryDAO: CategoryDAO
    init{
        LibraryApplication.instance.libraryComponent.inject(this)
    }
    override fun getDAO(): BaseDAO<CategoryEntity> {
        return categoryDAO
    }

    override fun getEntity(): LiveData<CategoryEntity> {
        return category
    }
    override fun setEntity(entity: CategoryEntity):  BOCategory {
        this.id = entity.id!!
        category= MutableLiveData(entity)
        return this
    }
    override fun find(id: Long): BOCategory {
        this.id = id
        category = categoryDAO.findCategory(id)!!
        books = boBook.findByCategory(id)

        return this
    }
    fun findAll(): MutableLiveData<List<CategoryEntity>> {
        categories =  categoryDAO.findAll()
        return categories as MutableLiveData<List<CategoryEntity>>
    }
    override fun delete() {
       books.value?.forEach({
           boBook.setEntity(it).delete()
        })
       super.delete()
    }
    fun refreshBooks(){
        if ( category.value == null) {
            if ( this.id == -1L )
                throw Exception("invalid state")
            else
               books = boBook.findByCategory(this.id)
        }
        else
             books = boBook.findByCategory(category.value?.id!!)
    }
    fun addBook(book: BookEntity) {
        boBook.setEntity(book).insert()
        refreshBooks()
    }
    fun deleteBook(book: BookEntity) {
        boBook.setEntity(book).delete()
    }
    fun updateBook(book: BookEntity) {
        boBook.setEntity(book).update()
        refreshBooks()
    }
}