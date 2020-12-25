package com.sample.libraryapplication.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.R
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.databinding.BookFragmentBinding
import com.sample.libraryapplication.utils.ActivityWeakMapRef
import com.sample.libraryapplication.view.MainActivity
import com.sample.libraryapplication.viewmodel.BookViewModel

class BookFragment  : Fragment() {
    companion object {
        val TAG = "BookFragment"
        val is_update_book = "is_update_book"
        val selected_book = "selected_book"
        val selected_category_id = "selected_category_id"
        @JvmStatic
        fun newInstance(updateBook: Boolean, book: BookEntity, categoryId: Long) =
            BookFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(is_update_book, updateBook)
                    putParcelable(selected_book, book)
                    putLong(selected_category_id,categoryId)
                }
            }
    }
    private lateinit var viewModel: BookViewModel
    private lateinit var binding: BookFragmentBinding
    private var isUpdateBook: Boolean = false
    private var selectedBook: BookEntity? = null
    private var selectedCategoryId: Long? = null
    private var container: ViewGroup? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.container = container
        ActivityWeakMapRef.put(TAG, this);
        injectDagger()
        if ( this::binding.isInitialized == false ) {
            binding = BookFragmentBinding.inflate(layoutInflater, container, false)
         }
        parseArguments()
        createViewModel()
        setBinding()
        observeViewModel()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private fun injectDagger() {

        LibraryApplication.instance.libraryComponent.inject(this)
    }

    private fun parseArguments() {
        if( requireArguments().get("is_update_book") !=null)
            isUpdateBook = requireArguments().getBoolean("is_update_book", false)
        selectedBook = requireArguments().getParcelable("selected_book") as? BookEntity
        selectedCategoryId = requireArguments().getLong("selected_category_id", -1)
    }

    private fun createViewModel() {
        if ( !this::viewModel.isInitialized)
            viewModel = ViewModelProvider(this).get(BookViewModel::class.java)
        if ( this::binding.isInitialized == false )
            binding = BookFragmentBinding.inflate(layoutInflater, container, false)
        viewModel.clear()
        binding.viewModel = viewModel
        viewModel.selectedCategoryId = selectedCategoryId
        viewModel.selectedBook = selectedBook
        viewModel.isUpdateBook = isUpdateBook
        viewModel.bookName = selectedBook?.bookName
        selectedBook?.bookUnitPrice?.let {
            viewModel.bookPrice = it.toString()
        }
    }

    private fun setBinding() {
        if ( this::binding.isInitialized == false )
            binding = BookFragmentBinding.inflate(layoutInflater, container, false)
        binding.viewModel = viewModel
        binding.book = selectedBook
        binding.lifecycleOwner = this
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun observeViewModel() {
        viewModel.isBookNameEmpty.observe(this, Observer {
            if (!isDetached) {
                if (it) binding.etName.error = getString(R.string.warning_book_name)
            }
        })

        viewModel.isBookPriceEmpty.observe(this, Observer {
            if (!isDetached) {
                if (it) binding.etPrice.error = getString(R.string.warning_book_price)
            }
        })

        viewModel.shouldFinishActivity.observe(this, Observer {
            if (it && !isDetached) {
                Handler(Looper.getMainLooper()).postDelayed({
                    var mainActivity = ActivityWeakMapRef.get(MainActivity.TAG) as MainActivity

                    mainActivity.selectItem(MainActivity.BOOK_LIST_MENU_NDX)
                    Log.d(BookListFragment.TAG, "Re-Routing to MainActivity")
                }, 600)

            }
        })
    }
}