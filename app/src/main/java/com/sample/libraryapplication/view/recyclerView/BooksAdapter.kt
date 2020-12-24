package com.sample.libraryapplication.view.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.R
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.databinding.ListItemBookBinding
import com.sample.libraryapplication.view.BookClickHandlers
import javax.inject.Inject


class BooksAdapter(private var bookList: List<BookEntity>?) : RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {
    @Inject
    lateinit var bookClickHandler: BookClickHandlers

    init{
        LibraryApplication.instance.libraryComponent.inject(this)
    }
    fun updateBookList(newBooksList: List<BookEntity>?) {
        val diffResult = DiffUtil.calculateDiff(BooksDiffCallback(bookList, newBooksList), false)
        bookList = newBooksList
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_item_book, parent, false))
    }

    override fun getItemCount(): Int {
        return bookList?.size ?: 0
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.dataBinding.setVariable(BR.book, bookList?.get(position))
        holder.dataBinding.setVariable(BR.clickHandlers, bookClickHandler)
//        holder.dataBinding.imageView.setImageResource(bookList!!.get(position).resourceId)
    }

    class BookViewHolder(binding: ListItemBookBinding) : RecyclerView.ViewHolder(binding.root) {
        var dataBinding = binding
    }
 }