package com.sample.libraryapplication.view.fragment

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
import com.sample.libraryapplication.databinding.CategoryListFragmentBinding

import com.sample.libraryapplication.utils.ActivityWeakMapRef
import com.sample.libraryapplication.service.MyMQTTHandler
import com.sample.libraryapplication.view.BookClickHandlers
import com.sample.libraryapplication.view.recyclerView.BooksAdapter
import com.sample.libraryapplication.view.recyclerView.CategoriesAdapter
import com.sample.libraryapplication.viewmodel.CategoryListFragmentViewModel
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@Suppress("DEPRECATION")
class CategoryListFragment : Fragment() {
    companion object {
        val TAG = "CategoryListFragment"
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
            CategoryListFragment().apply {
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
    lateinit var categoryListFragmentViewModel: CategoryListFragmentViewModel
    private lateinit var binding: CategoryListFragmentBinding
    @Inject
    lateinit var boCategory: BOCategory
    @Inject
    lateinit var bookClickHandlers: BookClickHandlers
    private var categoriesAdapter: CategoriesAdapter? = null
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
            return binding.root
        }

        ActivityWeakMapRef.put(TAG, this);
        injectDagger()
        createViewModel()
        setBinding()
        updateCategoryList2()
        updateCategoryList()

        binding.progressBar.visibility = View.VISIBLE

        //sumulate heavy DB work on the background
        Handler(Looper.getMainLooper()).postDelayed({
            categoryListFragmentViewModel.isLoading.value = false
            binding.progressBar.visibility = View.GONE
            Log.d(TAG, "onCreate: isLoading=true")
        }, 1000)

        roootView = binding.root

        return roootView
    }
    private fun createViewModel() {
        categoryListFragmentViewModel = ViewModelProvider(this).get(CategoryListFragmentViewModel::class.java)
    }
    private fun injectDagger() {
        LibraryApplication.instance.libraryComponent.inject(this)
    }
    private fun setBinding() {
        binding = CategoryListFragmentBinding.inflate(layoutInflater,container,false)
        binding.viewModel = categoryListFragmentViewModel
        binding.lifecycleOwner = this
        binding.clickHandlers = bookClickHandlers
    }
    private var observerCounter = 0
    inner class CategoriesObserver: Observer<List<CategoryEntity>>{
        init{
            observerCounter++
        }
        override fun onChanged(list: List<CategoryEntity>?) {
            if (list != null && !isDetached) {
                Log.d(TAG,"updateCategoryList::observer: observerCounter=${observerCounter} updating RecycleView via LiveData")
                list.forEach {
                    Log.d(TAG,"Category Name: ${it.categoryName} - Category Desc: ${it.categoryDesc}\"")
                }
                var listToUse = if (list.isNotEmpty()) list else listOf()
                showCategoryList(listToUse)
            }
        }
    }
    inner class CategoriesObserver2: Observer<Boolean>{
        init{
            observerCounter++
        }
        override fun onChanged(listUpdated: Boolean?) {
            val categoryList = categoryListFragmentViewModel.boCategory.categories.value
            if (categoryList != null && !isDetached) {
                Log.d(TAG,"updateCategoryList2::observer: observerCounter=${observerCounter} updating RecycleView via LiveData")
                categoryList.forEach {
                    Log.d(TAG,"Category Name: ${it.categoryName} - Category Desc: ${it.categoryDesc}\"")
                }
                var listToUse = if (categoryList.isNotEmpty()) categoryList else listOf()
                showCategoryList(listToUse)
            }
        }
    }
    private fun showCategoryList(categoryList: List<CategoryEntity>) {
        if (categoriesAdapter == null) {
            var recycler_view_categories = binding.recyclerViewCategories
            recycler_view_categories.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
            categoriesAdapter = CategoriesAdapter(categoryList)
            recycler_view_categories.adapter = categoriesAdapter
//            val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
//            itemTouchHelper.attachToRecyclerView(recycler_view_books)
        } else
            categoriesAdapter?.updateCategoryList(categoryList)
    }
    fun updateCategoryList2() {
        categoryListFragmentViewModel.boCategory.categories.removeObservers(this.viewLifecycleOwner)
        categoryListFragmentViewModel.boCategory.categories.observe(this.viewLifecycleOwner, CategoriesObserver())
    }
    fun updateCategoryList() {
        categoryListFragmentViewModel.boCategory.categoryListUpdated.removeObservers(this.viewLifecycleOwner)
        categoryListFragmentViewModel.boCategory.categoryListUpdated.observe(this.viewLifecycleOwner, CategoriesObserver2())
    }
    override fun onDestroy() {
        super.onDestroy()
        if ( categoryListFragmentViewModel.boCategory.booksInitalized())
            categoryListFragmentViewModel.boCategory.books.removeObservers(this.viewLifecycleOwner)
    }
}