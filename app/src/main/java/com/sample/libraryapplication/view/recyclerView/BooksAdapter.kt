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


class BooksAdapter @Inject constructor(private var bookList: List<BookEntity>?, val bookClickHandlers: BookClickHandlers) : RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {
    companion object{
        var cnt = 0;
        val images = arrayListOf<String>("https://www.palestineremembered.com/Acre/al-Bassa/Picture907.jpg",
                                        "https://www.palestineremembered.com/Acre/al-Bassa/Picture909.jpg",
                                        "https://www.palestineremembered.com/Acre/al-Bassa/Picture913.jpg",
                                        "https://www.palestineremembered.com/Acre/al-Bassa/Picture917.jpg",
                                        "https://www.palestineremembered.com/Acre/al-Bassa/Picture176.jpg",
                                        "https://www.palestineremembered.com/Acre/al-Bassa/Picture2983.jpg",
                                        "https://www.palestineremembered.com/Acre/al-Bassa/Picture2985.jpg",
                                        "https://www.palestineremembered.com/Acre/al-Bassa/Picture2987.jpg",
                                        "https://www.palestineremembered.com/Acre/al-Bassa/Picture7177.jpg",
                                        "https://www.palestineremembered.com/Acre/al-Bassa/Picture7179.jpg")

        fun getRandomImageURL(): String? {
            return images.get((cnt++) % images.size)
        }
    }

    init{
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
        val book = bookList?.get(position)
        book?.url = getRandomImageURL()
        holder.dataBinding.setVariable(BR.book, book)
        holder.dataBinding.setVariable(BR.clickHandlers, bookClickHandlers)
//        holder.dataBinding.imageView.setImageResource(bookList!!.get(position).resourceId)
    }



    class BookViewHolder(binding: ListItemBookBinding) : RecyclerView.ViewHolder(binding.root) {
        var dataBinding = binding
    }
 }