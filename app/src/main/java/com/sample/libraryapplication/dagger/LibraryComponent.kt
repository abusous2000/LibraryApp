package com.sample.libraryapplication.dagger

import com.sample.libraryapplication.bo.BOBook
import com.sample.libraryapplication.bo.BOCategory
import com.sample.libraryapplication.dagger.module.RoomDatabaseModule
import com.sample.libraryapplication.database.DBPopulator
import com.sample.libraryapplication.utils.MyMQTTHandler
import com.sample.libraryapplication.view.BookActivity
import com.sample.libraryapplication.view.BookListActivity
import com.sample.libraryapplication.view.BooksAdapter
import com.sample.libraryapplication.view.MainFragment
import com.sample.libraryapplication.viewmodel.BookListViewModel
import com.sample.libraryapplication.viewmodel.BookViewModel
import com.sample.libraryapplication.viewmodel.MainFragmentViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RoomDatabaseModule::class])
interface LibraryComponent {
    fun inject(roomDatabaseModule: RoomDatabaseModule)
    fun inject(boCategory: BOCategory)
    fun inject(booksAdapter: BooksAdapter)
    fun inject(bookListViewModel: BookListViewModel)
    fun inject(bookViewModel: BookViewModel)
    fun inject(bookActivity: BookActivity)
    fun inject(bookListActivity: BookListActivity)
    fun inject(boBook: BOBook)
    fun inject(dbPopulator: DBPopulator)
    fun inject(myMQTTHandler: MyMQTTHandler)
    fun inject(mainFragmentViewModel: MainFragmentViewModel)
    fun inject(mainFragment: MainFragment)
}