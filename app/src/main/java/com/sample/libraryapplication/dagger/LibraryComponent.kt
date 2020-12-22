package com.sample.libraryapplication.dagger

import com.sample.libraryapplication.bo.BOBook
import com.sample.libraryapplication.bo.BOCategory
import com.sample.libraryapplication.dagger.module.RoomDatabaseModule
import com.sample.libraryapplication.database.DBPopulator
import com.sample.libraryapplication.utils.MyMQTTHandler
import com.sample.libraryapplication.view.*
import com.sample.libraryapplication.viewmodel.BookViewModel
import com.sample.libraryapplication.viewmodel.BookListFragmentViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RoomDatabaseModule::class])
interface LibraryComponent {
    fun inject(roomDatabaseModule: RoomDatabaseModule)
    fun inject(boCategory: BOCategory)
    fun inject(booksAdapter: BooksAdapter)
    fun inject(bookViewModel: BookViewModel)
    fun inject(bookActivity: BookActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(boBook: BOBook)
    fun inject(dbPopulator: DBPopulator)
    fun inject(myMQTTHandler: MyMQTTHandler)
    fun inject(bookListFragmentViewModel: BookListFragmentViewModel)
    fun inject(bookListFragment: BookListFragment)
}