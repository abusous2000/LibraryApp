package com.sample.libraryapplication.dagger

import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.bo.BOBook
import com.sample.libraryapplication.bo.BOCategory
import com.sample.libraryapplication.dagger.module.RoomDatabaseModule
import com.sample.libraryapplication.database.DBPopulator
import com.sample.libraryapplication.service.MyMQTTHandler
import com.sample.libraryapplication.view.MainActivity
import com.sample.libraryapplication.view.fragment.BookFragment
import com.sample.libraryapplication.view.fragment.BookListFragment
import com.sample.libraryapplication.view.fragment.CategoryListFragment
import com.sample.libraryapplication.view.recyclerView.BooksAdapter
import com.sample.libraryapplication.view.recyclerView.CategoriesAdapter
import com.sample.libraryapplication.viewmodel.BookListFragmentViewModel
import com.sample.libraryapplication.viewmodel.BookViewModel
import com.sample.libraryapplication.viewmodel.CategoryListFragmentViewModel
import com.sample.libraryapplication.viewmodel.MQTTViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RoomDatabaseModule::class])
interface LibraryComponent {
    fun inject(roomDatabaseModule: RoomDatabaseModule)
    fun inject(boCategory: BOCategory)
    fun inject(booksAdapter: BooksAdapter)
    fun inject(bookViewModel: BookViewModel)
    fun inject(mainActivity: MainActivity)
    fun inject(boBook: BOBook)
    fun inject(dbPopulator: DBPopulator)
    fun inject(myMQTTHandler: MyMQTTHandler)
    fun inject(bookListFragmentViewModel: BookListFragmentViewModel)
    fun inject(bookListFragment: BookListFragment)
    fun inject(mqttViewModel: MQTTViewModel)
    fun inject(categoriesAdapter: CategoriesAdapter)
    fun inject(categoryListFragmentViewModel: CategoryListFragmentViewModel)
    fun inject(categoryListFragment: CategoryListFragment)
    fun inject(libraryApplication: LibraryApplication)
    fun inject(bookFragment: BookFragment)
}