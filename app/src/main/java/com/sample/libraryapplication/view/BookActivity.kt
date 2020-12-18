package com.sample.libraryapplication.view

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.R
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.databinding.ActivityBookBinding
import com.sample.libraryapplication.viewmodel.BookViewModel
//import kotlinx.android.synthetic.main.activity_book.*

class BookActivity : AppCompatActivity() {

    companion object {
        private val TAG = BookActivity::class.java.simpleName
    }

    private lateinit var bookViewModel: BookViewModel
    private var isUpdateBook: Boolean = false
    private var selectedBook: BookEntity? = null
    private var selectedCategoryId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        injectDagger()

        getIntentExtras()

        createViewModel()

        setBinding()

        observeViewModel()
    }

    private fun injectDagger() {
        LibraryApplication.instance.libraryComponent.inject(this)
    }

    private fun getIntentExtras() {
        if(intent?.extras != null && intent?.extras!!.containsKey("is_update_book"))
            isUpdateBook = intent.getBooleanExtra("is_update_book", false)
        selectedBook = intent?.getParcelableExtra("selected_book") as? BookEntity
        selectedCategoryId = intent?.getLongExtra("selected_category_id", -1)
    }

    private fun createViewModel() {
        bookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)
        bookViewModel.selectedCategoryId = selectedCategoryId
        bookViewModel.selectedBook = selectedBook
        bookViewModel.isUpdateBook = isUpdateBook
        bookViewModel.bookName = selectedBook?.bookName
        selectedBook?.bookUnitPrice?.let {
            bookViewModel.bookPrice = it.toString()
        }
    }

    private fun setBinding() {
        val binding = ActivityBookBinding.inflate(layoutInflater)
        binding.viewModel = bookViewModel
        binding.book = selectedBook
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

    private fun observeViewModel() {
        bookViewModel.isBookNameEmpty.observe(this, Observer {
            if (!isDestroyed) {
                if (it) findViewById<EditText>(R.id.et_name).error = getString(R.string.warning_book_name)
            }
        })

        bookViewModel.isBookPriceEmpty.observe(this, Observer {
            if (!isDestroyed) {
                if (it) findViewById<EditText>(R.id.et_price).error = getString(R.string.warning_book_price)
            }
        })

        bookViewModel.shouldFinishActivity.observe(this, Observer {
            if (!isDestroyed) {
                if (it) finish()
            }
        })
    }
}
