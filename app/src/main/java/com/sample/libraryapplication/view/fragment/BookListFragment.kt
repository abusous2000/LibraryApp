package com.sample.libraryapplication.view.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.sample.libraryapplication.R
import com.sample.libraryapplication.bo.BOCategory
import com.sample.libraryapplication.database.DBPopulator
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity
import com.sample.libraryapplication.databinding.BookListFragmentBinding
import com.sample.libraryapplication.databinding.ListItemBookBinding
import com.sample.libraryapplication.utils.ActivityWeakMapRef
import com.sample.libraryapplication.utils.CommonUtils
import com.sample.libraryapplication.view.recyclerView.GenericRecyclerViewAdapter
import com.sample.libraryapplication.view.BookClickHandlers
import com.sample.libraryapplication.view.MainActivity
import com.sample.libraryapplication.viewmodel.BookListFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.list_item_book.view.*
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class BookListFragment : BaseFragment() {
    companion object {
        val TAG = "BookListFragment"
    }
    val bookListViewModel: BookListFragmentViewModel by viewModels()
    lateinit var binding: BookListFragmentBinding
    @Inject
    lateinit var boCategory: BOCategory
    @Inject
    lateinit var bookClickHandlers: BookClickHandlers
    @Inject
    lateinit var dbPopulator: DBPopulator
    private var categoryArrayAdapter: ArrayAdapter<String>? = null
    private var booksAdapter: BooksRecyclerViewAdapter? = null
    private var selectedCategory: CategoryEntity? = null
    private var container: ViewGroup? = null

    override fun onCreatePersistentView(inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // I used data-binding
        Log.d(TAG, "onCreatePersistentView--->inflate: ")
        binding = BookListFragmentBinding.inflate(layoutInflater, container, false)
        setBinding()

//        ViewCompat.setLayoutDirection(binding.root, ViewCompat.LAYOUT_DIRECTION_RTL)
        return binding.root
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser){
            val mainActivity = ActivityWeakMapRef.get(MainActivity.TAG) as MainActivity
            mainActivity.supportActionBar?.title = TAG
        }
    }
    override fun onPersistentViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onPersistentViewCreated(view, savedInstanceState)
        ActivityWeakMapRef.put(TAG, this);
        setBinding()
       if (dbPopulator.doesDbExist(requireContext()) == false) {
            dbPopulator.dbPopulated.observe(viewLifecycleOwner, Observer {
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
        binding!!.progressBar.visibility = View.VISIBLE
        //simulate heavy DB work on the background
        Handler(Looper.getMainLooper()).postDelayed({
            bookListViewModel.isLoading.value = false
            binding!!.progressBar.visibility = View.GONE
            Log.d(TAG, "onCreate: isLoading=true")
        }, 500)
    }

    private fun postDBStart() {
        boCategory.categories = boCategory.findAll() as MutableLiveData<MutableList<CategoryEntity>>
        observeViewModel()
    }
    private fun setBinding() {
        if ( !this::binding.isInitialized)
            binding = BookListFragmentBinding.inflate(layoutInflater, container, false)
        binding!!.viewModel = bookListViewModel
        binding!!.lifecycleOwner = viewLifecycleOwner
        binding!!.clickHandlers = bookClickHandlers
    }
    fun observeViewModel() {
        //This is a hack, for some reason observer of categories are not notified only once
        boCategory.categoryListUpdated.observe(viewLifecycleOwner, Observer {
            if (it) {
                setDataToSpinner(boCategory.categories.value)
                boCategory.categoryListUpdated.postValue(false)
            }
        })
        bookListViewModel.isLoading.value = true
        boCategory.categories.observe(viewLifecycleOwner, Observer { list ->
            if (!isDetached) {
                bookListViewModel.isLoading.value = false
                list.forEach {
                    Log.d(TAG + ": observeViewModel", "${it.id}-->Category Name: ${it.categoryName} - Category Desc: ${it.categoryDesc}")
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
                    categoryArrayAdapter = ArrayAdapter(this.requireContext(),R.layout.list_item_category,catNames)
                    categoryArrayAdapter?.setDropDownViewResource(R.layout.list_item_category)
                    binding!!.spinnerAdapter = categoryArrayAdapter
                    binding!!.lifecycleOwner = this
                } else {
                    categoryArrayAdapter?.clear()
                    categoryArrayAdapter?.addAll(catNames)
                    categoryArrayAdapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private var observerCounter = 0
    inner class CategoryBooksObserver: Observer<List<BookEntity>>{
        init{
            observerCounter++
        }
        override fun onChanged(list: List<BookEntity>?) {
            if (list != null && !isDetached) {
                Log.d(TAG,"onChanged::observer: observerCounter=${observerCounter} updating RecycleView via LiveData w/ categoryId:" + selectedCategory?.id)
                list.forEach {
                    Log.d(TAG,"id: ${it.id}- Book: ${it.bookName}- Price: ${it.bookUnitPrice}- Category: ${it.bookCategoryID}\"")
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
    fun updateRVBookList(category: CategoryEntity) {
        selectedCategory=category
        bookListViewModel.boCategory.removeObservers(this)
        selectedCategory?.id?.let {
            bookListViewModel.getBooksListSelectedCategory(it)
            bookListViewModel.boCategory.books.observe(this, CategoryBooksObserver())
        }
    }
    private fun showBookList(bookList: List<BookEntity>): Boolean {
        if (booksAdapter == null) {
            val recycler_view_books = binding.recyclerViewBooks

            booksAdapter = BooksRecyclerViewAdapter(requireContext(),bookList)
            recycler_view_books.layoutManager = GridLayoutManager(context,3)
            recycler_view_books.adapter = booksAdapter
            booksAdapter?.getToucCallback()?.attachToRecyclerView(recycler_view_books)
            booksAdapter?.setOnLongClickListener(View.OnLongClickListener() {
                val bookName = it.findViewById<TextView>(R.id.bookName2)
//                Log.d(TAG, "OnLongClickListener: "+booksAdapter?.getCustomViewHolder(it as RecyclerView.ViewHolder)?.book?.url)
                Log.d(TAG, "OnLongClickListener: "+bookName?.text.toString())
                return@OnLongClickListener true
            })
        } else
            booksAdapter?.updateList(bookList)

        return true;
    }
    override fun onDestroy() {
        super.onDestroy()
        if ( bookListViewModel.boCategory.booksInitalized())
            bookListViewModel.boCategory.books.removeObservers(this)
    }

    inner class BooksRecyclerViewAdapter(context: Context, bookList: List<BookEntity>): GenericRecyclerViewAdapter<BookEntity, ListItemBookBinding>(context, bookList) {
        override fun getLayoutResId(): Int {
            return R.layout.list_item_book
        }
        override fun onBindData(book: BookEntity?, position: Int, dataBinding: ListItemBookBinding?) {
            book?.url = CommonUtils.getRandomImageURL()
            dataBinding?.setVariable(BR.book, book)
            dataBinding?.setVariable(BR.clickHandlers, bookClickHandlers)
        }
        override fun onItemClick(model: BookEntity?, position: Int) {
        }
        override fun onLeftSwip(viewHolder: RecyclerView.ViewHolder?) {
            bookListViewModel.deleteBook(booksAdapter?.getCustomViewHolder(viewHolder)?.book!!)
        }
    }
}

