package com.sample.libraryapplication.view.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.libraryapplication.R
import com.sample.libraryapplication.bo.BOCategory
import com.sample.libraryapplication.database.entity.CategoryEntity
import com.sample.libraryapplication.databinding.CategoryListFragmentBinding
import com.sample.libraryapplication.databinding.ListItemRvCategoryBinding
import com.sample.libraryapplication.utils.ActivityWeakMapRef
import com.sample.libraryapplication.view.recyclerView.GenericAdapter
import com.sample.libraryapplication.view.BookClickHandlers
import com.sample.libraryapplication.viewmodel.CategoryListFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryListFragment : Fragment() {
    companion object {
        val TAG = "CategoryListFragment"
    }

    private var rootView: View? = null
    val categoryListFragmentViewModel: CategoryListFragmentViewModel by viewModels()
    private lateinit var binding: CategoryListFragmentBinding

    @Inject
    lateinit var boCategory: BOCategory

    @Inject
    lateinit var bookClickHandlers: BookClickHandlers
    private var container: ViewGroup? = null

    private var categoriesAdapter : CategoriesAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.container = container

        ActivityWeakMapRef.put(TAG, this);
        if (rootView != null) {
            updateCategoryList()
            return binding.root
        }
        setBinding()
        updateCategoryList()
        binding.progressBar.visibility = View.VISIBLE

        //sumulate heavy DB work on the background
        Handler(Looper.getMainLooper()).postDelayed({
            categoryListFragmentViewModel.isLoading.value = false
            binding.progressBar.visibility = View.GONE
            Log.d(TAG, "onCreate: isLoading=true")
        }, 200)

        rootView = binding.root

        return rootView
    }

    private fun setBinding() {
        binding = CategoryListFragmentBinding.inflate(layoutInflater, container, false)
        binding.viewModel = categoryListFragmentViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.clickHandlers = bookClickHandlers
    }

    private var observerCounter = 0

    inner class CategoriesObserver : Observer<List<CategoryEntity>> {
        init {
            observerCounter++
        }

        override fun onChanged(list: List<CategoryEntity>?) {
            if (list != null && !isDetached) {
                var listToUse = if (list.isNotEmpty()) list else listOf()
                showCategoryList(listToUse)
            }
        }
    }

    inner class CategoriesObserver2 : Observer<Boolean> {
        init {
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
    private fun showCategoryList(categoryList: List<CategoryEntity>) {
        Log.d(TAG, "showCategoryList::observer: observerCounter=${observerCounter} updating RecycleView via LiveData")
        categoryList.forEach {
            Log.d(TAG, "Category Name: ${it.categoryName} - Category Desc: ${it.categoryDesc}\"")
        }
        if (categoriesAdapter == null) {
            var recycler_view_categories = binding.recyclerViewCategories
            recycler_view_categories.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            categoriesAdapter = CategoriesAdapter(requireContext(), categoryList)
            recycler_view_categories.adapter = categoriesAdapter
            categoriesAdapter?.getToucCallback()?.attachToRecyclerView(recycler_view_categories)
        }
        else
            categoriesAdapter?.updateList(categoryList)
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
        try {
            if (categoryListFragmentViewModel.boCategory.booksInitalized() && view != null) categoryListFragmentViewModel.boCategory.books.removeObservers(this.viewLifecycleOwner)
        }
        catch (t: Throwable) {
            Log.e(TAG, "onDestroy: ", t)
        }
        rootView = null
    }


    inner class CategoriesAdapter(context: Context, categoryList: List<CategoryEntity>) : GenericAdapter<CategoryEntity, ListItemRvCategoryBinding>(context, categoryList) {
        override fun getLayoutResId(): Int {
            return R.layout.list_item_rv_category
        }

        override fun onBindData(book: CategoryEntity?, position: Int, dataBinding: ListItemRvCategoryBinding?) {
            dataBinding?.setVariable(BR.category, mArrayList?.get(position))
            dataBinding?.setVariable(BR.clickHandlers, bookClickHandlers)
        }

        override fun onItemClick(model: CategoryEntity?, position: Int) {
        }

        override fun onLeftSwip(viewHolder: RecyclerView.ViewHolder?) {
            categoryListFragmentViewModel.deleteCategory(categoriesAdapter?.getCustomViewHolder(viewHolder)?.category!!)
        }
    }
}
