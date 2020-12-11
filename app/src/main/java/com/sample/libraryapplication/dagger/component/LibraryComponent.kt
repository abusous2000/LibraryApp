package com.sample.libraryapplication.dagger.component

import com.sample.libraryapplication.dagger.module.RoomDatabaseModule
import com.sample.libraryapplication.view.BookActivity
import com.sample.libraryapplication.view.BookListActivity
import com.sample.libraryapplication.view.BooksAdapter
import com.sample.libraryapplication.viewmodel.BookListViewModel
import com.sample.libraryapplication.viewmodel.BookViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RoomDatabaseModule::class])
interface LibraryComponent {
    fun inject(booksAdapter: BooksAdapter)
    fun inject(bookListViewModel: BookListViewModel)
    fun inject(bookViewModel: BookViewModel)
    fun inject(bookActivity: BookActivity)
    fun inject(bookListActivity: BookListActivity)
}