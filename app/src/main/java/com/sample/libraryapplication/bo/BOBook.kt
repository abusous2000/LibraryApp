package com.sample.libraryapplication.bo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.database.dao.BaseDAO
import com.sample.libraryapplication.database.dao.BookDAO
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity
import javax.inject.Inject

class BOBook @javax.inject.Inject constructor(): BOAbstract<BookEntity>() {
    lateinit var book: LiveData<BookEntity>
    @Inject
    lateinit var bookDAO: BookDAO
    init{
        LibraryApplication.instance.libraryComponent.inject(this)
    }
    override fun getDAO(): BaseDAO<BookEntity> {
        return bookDAO;
    }

    override fun getEntity(): LiveData<BookEntity>? {
        return book
    }

    override fun setEntity(entity: BookEntity): BOBook {
        book = MutableLiveData(entity)
        return this
    }
    fun findByCategory(id: Long): LiveData<List<BookEntity>>{
        return bookDAO.findAllByCategory(id)
    }

    override fun find(id: Long): BOAbstract<BookEntity> {
        book = bookDAO.findBook(id)!!
        return this
    }
}