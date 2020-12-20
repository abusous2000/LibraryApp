package com.sample.libraryapplication.view

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBarDrawerToggle
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
import javax.inject.Inject


class BookListActivity : AppCompatActivity() {
    companion object {
        val TAG = BookListActivity::class.java.simpleName
    }
    @Inject
    lateinit var boCategory: BOCategory
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
            if (it) {
                setDataToSpinner(boCategory.categories.value)
                boCategory.categoryListUpdated.postValue(false)
            }
        })
        bookListViewModel.isLoading.value = true
        boCategory.categories.observe(this, Observer { list ->
            if (!isDestroyed) {
                bookListViewModel.isLoading.value = false
                list.forEach {
                    Log.d(
                        TAG + ": observeViewModel",
                        "${it.id}-->Category Name: ${it.categoryName} - Category Desc: ${it.categoryDesc}"
                    )
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
                     categoryArrayAdapter = ArrayAdapter(
                         this,
                         R.layout.list_item_category,
                         catNames
                     )
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
    data class DataModel(var icon: Int, var name: String)
    lateinit var drawerItemTitles: Array<String>
    inner class DrawerItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            selectItem(position)
        }
    }

    private fun selectItem(position: Int) {
        Log.d(TAG, "onOptionsItemSelected: Item $position was clicked")

        binding.leftDrawer.setItemChecked(position, true)
        binding.leftDrawer.setSelection(position)
        binding.drawerLayout.closeDrawer(binding.leftDrawer)
        setTitle(drawerItemTitles[position])

    }
    private fun setBinding() {
        binding = ActivityBookListBinding.inflate(layoutInflater)
        binding.viewModel = bookListViewModel
        binding.lifecycleOwner = this
        binding.clickHandlers = bookClickHandlers

        binding.topAppBar.setOnMenuItemClickListener(OnMenuItemClickListener@{
            Log.d(TAG, "onOptionsItemSelected: Item ${it?.itemId} was clicked")
            bookClickHandlers.onMenuClicked(it)
            return@OnMenuItemClickListener true
        })



        val drawerItem= mutableListOf<DataModel>()
        drawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        drawerItem.add( DataModel(R.drawable.connect, drawerItemTitles[0]) )
        drawerItem.add( DataModel(R.drawable.fixtures, drawerItemTitles[1]) )
        drawerItem.add( DataModel(R.drawable.table, drawerItemTitles[2]) )

        val adapter = DrawerItemCustomAdapter(this, R.layout.menu_view_item_row, drawerItem)
        binding.leftDrawer.adapter =adapter
        binding.leftDrawer.onItemClickListener = DrawerItemClickListener()
//        setSupportActionBar(binding.topAppBar);
        var toggle = ActionBarDrawerToggle(this,  binding.drawerLayout, binding.topAppBar, R.string.app_name, R.string.app_name )
        toggle.syncState()
        binding.drawerLayout.addDrawerListener(toggle)
//        supportActionBar?.setDisplayShowTitleEnabled(true);
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
                Log.d(
                    TAG,
                    "updateBookList::observer: observerCounter=${observerCounter} updating RecycleView via LiveData for books with categoryId:" + selectedCategory?.id
                )
                list.forEach {
                    Log.d(
                        TAG,
                        "Book Name: ${it.bookName} - Book Price: ${it.bookUnitPrice}- Category: ${it.bookCategoryID}\""
                    )
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
            var recycler_view_books = findViewById<RecyclerView>(R.id.recycler_view_books)
            recycler_view_books.layoutManager = LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
            )
            booksAdapter = BooksAdapter(bookList)
            recycler_view_books.adapter = booksAdapter
            val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
            itemTouchHelper.attachToRecyclerView(recycler_view_books)
        } else
            booksAdapter?.updateBookList(bookList)
    }

    private var simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            if (swipeDir == ItemTouchHelper.LEFT) {
                if (viewHolder is BooksAdapter.BookViewHolder) {
                    bookListViewModel.deleteBook(viewHolder.dataBinding.book as BookEntity)
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
        myMQTTHandler.close()
    }
}
