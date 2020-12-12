package com.sample.libraryapplication.bo

import androidx.lifecycle.LiveData
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.database.dao.BaseDAO
import com.sample.libraryapplication.database.dao.BookDAO
import com.sample.libraryapplication.database.dao.CategoryDAO
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity
import javax.inject.Inject

class BOCategory @Inject constructor(): BOAbstract<CategoryEntity>(){
    private lateinit var category: CategoryEntity
    lateinit var books: LiveData<List<BookEntity>>
    @Inject
    lateinit var bookDAO: BookDAO
    @Inject
    lateinit var categoryDAO: CategoryDAO
    init{
        LibraryApplication.instance.libraryComponent.inject(this)
    }
    override fun getDAO(): BaseDAO<CategoryEntity> {
        return categoryDAO
    }

    override fun getEntity(): CategoryEntity {
        return category
    }
    fun find(id: Long): BOCategory {
        category = categoryDAO.find(id)!!
        books = bookDAO.findAllByCategory(id)

        return this
    }
    fun findAll(): LiveData<List<CategoryEntity>> {
        return categoryDAO.findAll()
    }
    override fun delete() {
       books.value?.forEach({
            bookDAO.delete(it)
        })
       super.delete()
    }
    fun refreshBooks(){
        if ( category != null && category.id !=null )
          books = bookDAO.findAllByCategory(category.id!!)
    }
    fun addBook(book: BookEntity) {
        bookDAO.insert(book)
        refreshBooks()
    }
    fun deleteBook(book: BookEntity) {
        bookDAO.delete(book)
        refreshBooks()
    }
    fun updateBook(book: BookEntity) {
        bookDAO.update(book)
        refreshBooks()
    }
}