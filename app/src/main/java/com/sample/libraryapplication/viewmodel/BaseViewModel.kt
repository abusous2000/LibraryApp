package com.sample.libraryapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.sample.libraryapplication.database.LibraryDatabase
import com.sample.libraryapplication.repository.BookRepository
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {
    @Inject
    lateinit var libraryDatabase: LibraryDatabase

    init {
        registerWithComponent()
    }
    abstract fun registerWithComponent()
}