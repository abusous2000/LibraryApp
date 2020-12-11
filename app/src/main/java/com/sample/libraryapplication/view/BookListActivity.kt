package com.sample.libraryapplication.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.R
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity
import com.sample.libraryapplication.databinding.ActivityBookListBinding
import com.sample.libraryapplication.viewmodel.BookListViewModel
import kotlinx.android.synthetic.main.activity_book_list.*
import java.lang.ref.WeakReference
import javax.inject.Inject


class BookListActivity : AppCompatActivity() {

    companion object {
        val TAG = BookListActivity::class.java.simpleName
    }
    @Inject
    lateinit var bookClickHandlers: BookClickHandlers
    private lateinit var binding: ActivityBookListBinding
    private lateinit var bookListViewModel: BookListViewModel
    private var categoryArrayAdapter: ArrayAdapter<CategoryEntity>? = null
    private var booksAdapter: BooksAdapter? = null
    private var selectedCategory: CategoryEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityWeakRef.updateActivity(TAG,this);
        injectDagger()

        createViewModel()

        setBinding()

        observeViewModel()
    }

    private fun createViewModel() {
        bookListViewModel = ViewModelProvider(this).get(BookListViewModel::class.java)
    }

    private fun injectDagger() {
        LibraryApplication.instance.libraryComponent.inject(this)
    }

    private fun observeViewModel() {
        bookListViewModel.isLoading.value = true
        bookListViewModel.allCategories.observe(this, Observer { list ->
            if (!isDestroyed) {
                bookListViewModel.isLoading.value = false
                list.forEach {
                    Log.d(TAG, "Category Name: ${it.categoryName} - Category Desc: ${it.categoryDesc}")
                }
                setDataToSpinner(list)
            }
        })
    }

    private fun setDataToSpinner(categoryList: List<CategoryEntity>?) {
        categoryList?.let { list ->
            if (list.isNotEmpty()) {
                if (categoryArrayAdapter == null) {
                    categoryArrayAdapter = ArrayAdapter(this, R.layout.list_item_category, list)
                    categoryArrayAdapter?.setDropDownViewResource(R.layout.list_item_category)
                    binding.spinnerAdapter = categoryArrayAdapter
                } else {
                    categoryArrayAdapter?.clear()
                    categoryArrayAdapter?.addAll(list)
                    categoryArrayAdapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun setBinding() {
        binding = ActivityBookListBinding.inflate(layoutInflater)
        binding.viewModel = bookListViewModel
        binding.lifecycleOwner = this
        binding.clickHandlers = bookClickHandlers
        setContentView(binding.root)
    }

    fun updateBookList(categoryID: Long?) {
        selectedCategory?.id = categoryID
        categoryID?.let {
            Log.d(TAG, "categoryID: $categoryID")
            bookListViewModel.getBooksListSelectedCategory(categoryID).observe(this, Observer { list ->
                if (!isDestroyed) {
                    Log.d(TAG, "updateBookList::observer")
                    list.forEach {
                        Log.d(TAG, "Book Name: ${it.bookName} - Book Price: ${it.bookUnitPrice}")
                    }
                    if (list.isNotEmpty()) {
                        if (list[0].bookCategoryID == categoryID)
                            showBookList(list)
                    } else
                        showBookList(listOf())
                }
            })
        }
    }

    private fun showBookList(bookList: List<BookEntity>) {
        if (booksAdapter == null) {
            recycler_view_books.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            booksAdapter = BooksAdapter(bookList)
            recycler_view_books.adapter = booksAdapter
            val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
            itemTouchHelper.attachToRecyclerView(recycler_view_books)
        } else
            booksAdapter?.updateBookList(bookList)
    }

    private var simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            if (swipeDir == ItemTouchHelper.LEFT) {
                if (viewHolder is BooksAdapter.BookViewHolder) {
                    bookListViewModel.deleteBook(viewHolder.dataBinding.book as BookEntity)
                    setUpdatedBookList()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setUpdatedBookList()
    }

    /**
     * This method is needed because of Livedata and room library's queries both work asynchronously.
     * Called after insert-update-delete processes
     * */
    private fun setUpdatedBookList() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isDestroyed) {
                Log.d(TAG, "setUpdatedBookList called")
                updateBookList(selectedCategory?.id)
            }
        }, 100)
    }
}
