package com.sample.libraryapplication.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.R
import com.sample.libraryapplication.bo.BOCategory
import com.sample.libraryapplication.database.DBPopulator
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity
import com.sample.libraryapplication.databinding.ActivityBookListBinding
import com.sample.libraryapplication.utils.MyMQTTHandler
import com.sample.libraryapplication.viewmodel.BookListViewModel
import kotlinx.android.synthetic.main.activity_book_list.*
import javax.inject.Inject


class BookListActivity : AppCompatActivity() {
    companion object {
        val TAG = BookListActivity::class.java.simpleName
    }
    @Inject
    lateinit var bookClickHandlers: BookClickHandlers
    @Inject
    lateinit var myMQTTHandler: MyMQTTHandler

    private lateinit var binding: ActivityBookListBinding
    lateinit var bookListViewModel: BookListViewModel
    private var categoryArrayAdapter: ArrayAdapter<String>? = null
    private var booksAdapter: BooksAdapter? = null
    private var selectedCategory: CategoryEntity? = null
    @Inject
    lateinit var dbPopulator: DBPopulator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityWeakRef.updateActivity(TAG, this);
        injectDagger()
        createViewModel()
        setBinding()

        if (dbPopulator.doesDbExist(this) == false) {
            dbPopulator.dbPopulated.observe(this, Observer {
                    if (it) {
                        postDBStart()
                    }
            })

            Handler(Looper.getMainLooper()).postDelayed({
                    Log.d(TAG, "onCreate: repopulating DB from main thread")
                    dbPopulator.populateDB()
                }, 100)
       } else{
            postDBStart()
        }
        myMQTTHandler.connect(this)
    }

    private fun postDBStart() {
        boCategory.categories = boCategory.findAll() as MutableLiveData<MutableList<CategoryEntity>>
        observeViewModel()
    }

    private fun createViewModel() {
        bookListViewModel = ViewModelProvider(this).get(BookListViewModel::class.java)
    }

    private fun injectDagger() {
        LibraryApplication.instance.libraryComponent.inject(this)
    }

    fun observeViewModel() {
        //This is a hack, for some reason observer of categories are not notified only once
        boCategory.categoryListUpdated.observe(this, Observer {
            if ( it ) {
                setDataToSpinner(boCategory.categories.value)
                boCategory.categoryListUpdated.postValue(false)
            }
        })
        bookListViewModel.isLoading.value = true
        boCategory.categories.observe(this, Observer { list ->
            if (!isDestroyed) {
                bookListViewModel.isLoading.value = false
                list.forEach {
                    Log.d(TAG+": observeViewModel", "${it.id}-->Category Name: ${it.categoryName} - Category Desc: ${it.categoryDesc}")
                }
                setDataToSpinner(list)
            }
        })
    }

    private fun setDataToSpinner(categoryList: List<CategoryEntity>?) {
        categoryList?.let { list ->
            if (list.isNotEmpty()) {
                var catNames = list.map { it.categoryName }.toList()
                if (categoryArrayAdapter == null) {
                     categoryArrayAdapter = ArrayAdapter(this, R.layout.list_item_category, catNames)
                    categoryArrayAdapter?.setDropDownViewResource(R.layout.list_item_category)
                    binding.spinnerAdapter = categoryArrayAdapter
                    binding.lifecycleOwner = this
                } else {
                    categoryArrayAdapter?.clear()
                    categoryArrayAdapter?.addAll(catNames)
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
    fun updateBookList(category: CategoryEntity) {
        selectedCategory=category
        bookListViewModel.boCategory.removeObserver(this)
         selectedCategory?.id?.let {
              bookListViewModel.getBooksListSelectedCategory(it)
              bookListViewModel.boCategory.books.observe(this, CategoryBooksObserver())
        }
    }
    private var observerCounter = 0
    inner class CategoryBooksObserver: Observer<List<BookEntity>>{
        init{
            observerCounter++
        }
        override fun onChanged(list: List<BookEntity>?) {
            if (list != null && !isDestroyed) {
                Log.d(TAG, "updateBookList::observer: observerCounter=${observerCounter} updating RecycleView via LiveData for books with categoryId:" + selectedCategory?.id)
                list.forEach {
                    Log.d(TAG, "Book Name: ${it.bookName} - Book Price: ${it.bookUnitPrice}- Category: ${it.bookCategoryID}\"")
                }
                if (list.isNotEmpty()) {
                    if (list[0].bookCategoryID == selectedCategory?.id)
                        showBookList(list)
                    else
                        Log.d(TAG, "no need to update list")
                } else
                    showBookList(listOf())
            }
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
                    //I found no need for this, it is here for record keeping
                    //setUpdatedBookList()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //I found no need for this, it is here for record keeping
        //setUpdatedBookList()
    }

    override fun onDestroy() {
        super.onDestroy()
        if ( bookListViewModel.boCategory.booksInitalized())
            bookListViewModel.boCategory.books.removeObservers(this)
        myMQTTHandler.disconnect()
    }
    /**
     * This method is needed because of Livedata and room library's queries both work asynchronously.
     * Called after insert-update-delete processes
     * */
    @Inject
    lateinit var boCategory: BOCategory

    val delay :Long = 2500
    inner class ServeCategoryList(val handler: Handler, var delay: Long = 2500) : Runnable {
        override fun run() {
            if (!isDestroyed) {
                Log.d(TAG, "setUpdatedBookList called")
                if (selectedCategory == null) {
                    if (boCategory.categories.value?.size!! > 0)
                        selectedCategory = boCategory.categories.value?.get(0)
                }
                if (selectedCategory != null)
                    updateBookList(selectedCategory!!)
                else {
                    Log.d(TAG, "re-running ServeCategoryList in: $delay")
                    handler.postDelayed(this, delay)
                }
            }
        }
    }
    //I found no need for this, it is here for record keeping
    private fun setUpdatedBookList() {
        var localHandler = Handler(Looper.getMainLooper())

        localHandler.postDelayed( ServeCategoryList(localHandler), delay)
    }

}
