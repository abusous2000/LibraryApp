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
import androidx.fragment.app.viewModels
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
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryListFragment : Fragment() {
    companion object {
        val TAG = "CategoryListFragment"
    }
    private var roootView: View? = null
    val categoryListFragmentViewModel: CategoryListFragmentViewModel by viewModels()
    private lateinit var binding: CategoryListFragmentBinding
    @Inject
    lateinit var boCategory: BOCategory
    @Inject
    lateinit var bookClickHandlers: BookClickHandlers
    private var categoriesAdapter: CategoriesAdapter? = null
    private var container: ViewGroup? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.container = container
        if (roootView !=null ) {
            updateCategoryList()
            return binding.root
        }

        ActivityWeakMapRef.put(TAG, this);
        injectDagger()
        createViewModel()
        setBinding()
        updateCategoryList()

        binding.progressBar.visibility = View.VISIBLE

        //sumulate heavy DB work on the background
        Handler(Looper.getMainLooper()).postDelayed({
            categoryListFragmentViewModel.isLoading.value = false
            binding.progressBar.visibility = View.GONE
            Log.d(TAG, "onCreate: isLoading=true")
        }, 200)

        roootView = binding.root

        return roootView
    }
    private fun createViewModel() {
//        categoryListFragmentViewModel = ViewModelProvider(this).get(CategoryListFragmentViewModel::class.java)
    }
    private fun injectDagger() {
    }
    private fun setBinding() {
        binding = CategoryListFragmentBinding.inflate(layoutInflater,container,false)
        binding.viewModel = categoryListFragmentViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.clickHandlers = bookClickHandlers
    }
    private var observerCounter = 0
    inner class CategoriesObserver: Observer<List<CategoryEntity>>{
        init{
            observerCounter++
        }
        override fun onChanged(list: List<CategoryEntity>?) {
            if (list != null && !isDetached) {
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
                var listToUse = if (categoryList.isNotEmpty()) categoryList else listOf()
                showCategoryList(listToUse)
            }
        }
    }
    private var simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        override fun onMove(recyclerView: RecyclerView,  viewHolder: RecyclerView.ViewHolder,target: RecyclerView.ViewHolder ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            if (swipeDir == ItemTouchHelper.LEFT) {
                if (viewHolder is CategoriesAdapter.CategoryViewHolder) {
                    categoryListFragmentViewModel.deleteCategory(viewHolder.dataBinding.category as CategoryEntity)
                }
            }
        }
    }

    private fun showCategoryList(categoryList: List<CategoryEntity>) {
        Log.d(TAG,"showCategoryList::observer: observerCounter=${observerCounter} updating RecycleView via LiveData")
        categoryList.forEach {
            Log.d(TAG,"Category Name: ${it.categoryName} - Category Desc: ${it.categoryDesc}\"")
        }
        if (categoriesAdapter == null) {
            var recycler_view_categories = binding.recyclerViewCategories
            recycler_view_categories.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
            categoriesAdapter = CategoriesAdapter(categoryList,bookClickHandlers)
            recycler_view_categories.adapter = categoriesAdapter
            val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
            itemTouchHelper.attachToRecyclerView(recycler_view_categories)
        } else
            categoriesAdapter?.updateCategoryList(categoryList)
    }
    //For some reason I have to to observe on two variables; I know this is a hack
    fun updateCategoryList() {
        categoryListFragmentViewModel.boCategory.categories.removeObservers(this.viewLifecycleOwner)
        categoryListFragmentViewModel.boCategory.categories.observe(this.viewLifecycleOwner, CategoriesObserver())
        categoryListFragmentViewModel.boCategory.categoryListUpdated.removeObservers(this.viewLifecycleOwner)
        categoryListFragmentViewModel.boCategory.categoryListUpdated.observe(this.viewLifecycleOwner, CategoriesObserver2())
    }
    override fun onDestroy() {
        super.onDestroy()
        try{
            if ( categoryListFragmentViewModel.boCategory.booksInitalized() && view != null )
                categoryListFragmentViewModel.boCategory.books.removeObservers(this.viewLifecycleOwner)
        }
        catch( t: Throwable){
            Log.e(TAG, "onDestroy: ", t)
        }
     }
}