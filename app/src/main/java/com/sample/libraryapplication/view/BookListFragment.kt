package com.sample.libraryapplication.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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
import com.sample.libraryapplication.databinding.BookListFragmentBinding

import com.sample.libraryapplication.utils.ActivityWeakMapRef
import com.sample.libraryapplication.utils.MyMQTTHandler
import com.sample.libraryapplication.viewmodel.BookListFragmentViewModel
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@Suppress("DEPRECATION")
class BookListFragment : Fragment() {
    companion object {
        val TAG = "MainFragment"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var roootView: View? = null
    lateinit var bookListViewModel: BookListFragmentViewModel
    private lateinit var binding: BookListFragmentBinding
    @Inject
    lateinit var boCategory: BOCategory
    @Inject
    lateinit var bookClickHandlers: BookClickHandlers
    @Inject
    lateinit var myMQTTHandler: MyMQTTHandler
    @Inject
    lateinit var dbPopulator: DBPopulator

    private var categoryArrayAdapter: ArrayAdapter<String>? = null
    private var booksAdapter: BooksAdapter? = null
    private var selectedCategory: CategoryEntity? = null
    private var container: ViewGroup? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.container = container
        if (roootView !=null ) {

            Handler(Looper.getMainLooper()).postDelayed({
                Log.d(TAG, "onCreate: repopulating DB from main thread")
                bookClickHandlers.onCategorySelected2(null,null,0,0)
            }, 200)

            return binding.root
        }

        ActivityWeakMapRef.put(BookListFragment.TAG, this);
        injectDagger()
        createViewModel()
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
        myMQTTHandler.connect(requireContext())
        binding.progressBar.visibility = View.VISIBLE

        //sumulate heavy DB work on the background
        Handler(Looper.getMainLooper()).postDelayed({
            bookListViewModel.isLoading.value = false
            binding.progressBar.visibility = View.GONE
            Log.d(TAG, "onCreate: isLoading=true")
        }, 1000)

        roootView = binding.root

        return roootView
    }
    private fun postDBStart() {
        boCategory.categories = boCategory.findAll() as MutableLiveData<MutableList<CategoryEntity>>
        observeViewModel()
    }
    private fun createViewModel() {
        bookListViewModel = ViewModelProvider(this).get(BookListFragmentViewModel::class.java)
    }
    private fun injectDagger() {
        LibraryApplication.instance.libraryComponent.inject(this)
    }
    private fun setBinding() {
        binding = BookListFragmentBinding.inflate(layoutInflater,container,false)
        binding.viewModel = bookListViewModel
        binding.lifecycleOwner = this
        binding.clickHandlers = bookClickHandlers
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
                    Log.d(BookListFragment.TAG + ": observeViewModel", "${it.id}-->Category Name: ${it.categoryName} - Category Desc: ${it.categoryDesc}"
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
                    categoryArrayAdapter = ArrayAdapter(this.requireContext(),R.layout.list_item_category,catNames)
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
            if (list != null && !isDetached) {
                Log.d(BookListFragment.TAG,
                    "updateBookList::observer: observerCounter=${observerCounter} updating RecycleView via LiveData for books with categoryId:" + selectedCategory?.id)
                list.forEach {
                    Log.d(
                        BookListFragment.TAG,
                        "Book Name: ${it.bookName} - Book Price: ${it.bookUnitPrice}- Category: ${it.bookCategoryID}\""
                    )
                }
                if (list.isNotEmpty()) {
                    if (list[0].bookCategoryID == selectedCategory?.id)
                        showBookList(list)
                    else
                        Log.d(BookListFragment.TAG, "no need to update list")
                } else
                    showBookList(listOf())
            }
        }
    }
    private fun showBookList(bookList: List<BookEntity>) {
        if (booksAdapter == null) {
            var recycler_view_books = binding.recyclerViewBooks
            recycler_view_books.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
            booksAdapter = BooksAdapter(bookList)
            recycler_view_books.adapter = booksAdapter
            val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
            itemTouchHelper.attachToRecyclerView(recycler_view_books)
        } else
            booksAdapter?.updateBookList(bookList)
    }
    private var simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
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

    override fun onDestroy() {
        super.onDestroy()
        if ( bookListViewModel.boCategory.booksInitalized())
            bookListViewModel.boCategory.books.removeObservers(this)
        myMQTTHandler.close()
    }
}