package com.sample.libraryapplication.view

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookClickHandlers @Inject constructor(){
    companion object {
        val TAG = BookClickHandlers::class.java.simpleName
    }
    private var selectedCategory: CategoryEntity? = null
    fun onBookItemClicked(view: View, book: BookEntity) {
        val intent = Intent(view.context, BookActivity::class.java)
        intent.putExtra("selected_category_id", book.bookCategoryID)
        intent.putExtra("selected_book", book)
        intent.putExtra("is_update_book", true)
        view.context.startActivity(intent)
    }
    fun onFABClicked(view: View) {
        val intent = Intent(view.context, BookActivity::class.java)
        intent.putExtra("selected_category_id", selectedCategory?.id)
        intent.putExtra("selected_category_id", selectedCategory?.id)
        view.context.startActivity(intent)
    }

    fun onCategorySelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d(BookClickHandlers.TAG, "onCategorySelected:$position")
        var bookListActivity = ActivityWeakRef.activityMap.get(BookListActivity.TAG)?.get() as BookListActivity
        //Check if the list was populated. It could be empty on startup since the DB takes longer to populate
        if ( bookListActivity.boCategory.categories?.value?.size!! > 0 )
            selectedCategory = bookListActivity.boCategory.categories?.value!!.get(position)
        if ( selectedCategory != null )
            bookListActivity.updateBookList(selectedCategory!!)
    }

}