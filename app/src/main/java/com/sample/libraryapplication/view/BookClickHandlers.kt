package com.sample.libraryapplication.view


import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.PopupMenu
import android.widget.Toast
import android.widget.Toolbar
import com.sample.libraryapplication.R
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity
import com.sample.libraryapplication.utils.ActivityWeakMapRef
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BookClickHandlers @Inject constructor(): PopupMenu.OnMenuItemClickListener,
    Toolbar.OnMenuItemClickListener {
    companion object {
        val TAG = BookClickHandlers::class.java.simpleName
    }
    private var selectedCategory: CategoryEntity? = null
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return this.onMenuClicked(item)
    }
    fun onBookItemClicked(view: View, book: BookEntity) {
        val intent = Intent(view.context, BookActivity::class.java)
        intent.putExtra("selected_category_id", book.bookCategoryID)
        intent.putExtra("selected_book", book)
        intent.putExtra("is_update_book", true)
        view.context.startActivity(intent)
    }
    fun onFABClicked(view: View) {
        val popup = PopupMenu(view.context, view)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()

    }
    fun onFABClicked2(view: Activity) {
        val intent = Intent(view, BookActivity::class.java)
        intent.putExtra("selected_category_id", selectedCategory?.id)
        intent.putExtra("selected_category_id", selectedCategory?.id)
        view.startActivity(intent)
    }
    fun onFABClicked3(view: Activity) {
        val intent = Intent(view, BookListActivity::class.java)

        view.startActivity(intent)
    }
    fun onFABClicked4(view: View) {
        val intent = Intent(view.context, BookListActivity::class.java)

        view.context.startActivity(intent)
    }
    fun onMenuClicked(menuItem: MenuItem?): Boolean {
        var bookListActivity = ActivityWeakMapRef.weakMap.get(BookListActivity.TAG) as BookListActivity
        when(menuItem?.itemId){
            R.id.Add_A_Book_id -> onFABClicked2(bookListActivity)
            else -> Toast.makeText(bookListActivity,"Item ${menuItem?.itemId} was clicked", Toast.LENGTH_SHORT ).show();
        }
        return true;
   }

//    fun onCategorySelected( @Suppress("UNUSED_PARAMETER") parent: AdapterView<*>?,
//                            @Suppress("UNUSED_PARAMETER") view: View?, position: Int,
//                            @Suppress("UNUSED_PARAMETER") id: Long) {
//        Log.d(BookClickHandlers.TAG, "onCategorySelected:$position")
//        var bookListActivity = ActivityWeakRef.activityMap.get(BookListActivity.TAG)?.get() as BookListActivity
//        //Check if the list was populated. It could be empty on startup since the DB takes longer to populate
//        if ( bookListActivity.boCategory.categories.value?.size!! > 0 )
//            selectedCategory = bookListActivity.boCategory.categories.value!!.get(position)
//        if ( selectedCategory != null )
//            bookListActivity.updateBookList(selectedCategory!!)
//    }
    fun onCategorySelected2( @Suppress("UNUSED_PARAMETER") parent: AdapterView<*>?,
                            @Suppress("UNUSED_PARAMETER") view: View?, position: Int,
                            @Suppress("UNUSED_PARAMETER") id: Long) {
        Log.d(BookClickHandlers.TAG, "onCategorySelected:$position")
        var mainFragment = ActivityWeakMapRef.weakMap.get(MainFragment.TAG) as MainFragment
        //Check if the list was populated. It could be empty on startup since the DB takes longer to populate
        if ( mainFragment.boCategory.categories.value?.size!! > 0 )
            selectedCategory = mainFragment.boCategory.categories.value!!.get(position)
        if ( selectedCategory != null )
            mainFragment.updateBookList(selectedCategory!!)
    }

}