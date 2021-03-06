package com.sample.libraryapplication.view.recyclerView

import androidx.recyclerview.widget.DiffUtil
import com.sample.libraryapplication.database.entity.BookEntity

class BooksDiffCallback(private val oldBooksList: List<BookEntity>?, private val newBooksList: List<BookEntity>?) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldBooksList?.get(oldItemPosition)?.id == newBooksList?.get(newItemPosition)?.id
    }

    override fun getOldListSize(): Int {
        return oldBooksList?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newBooksList?.size ?: 0
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldBooksList?.get(oldItemPosition)?.equals(newBooksList?.get(newItemPosition))!!
    }
}