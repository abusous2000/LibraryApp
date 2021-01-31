package com.sample.libraryapplication.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.R
import com.sample.libraryapplication.database.entity.CategoryEntity
import com.sample.libraryapplication.databinding.CategoryFragmentBinding
import com.sample.libraryapplication.utils.ActivityWeakMapRef
import com.sample.libraryapplication.view.MainActivity
import com.sample.libraryapplication.viewmodel.CategoryViewModel

class CategoryFragment  : Fragment() {
    companion object {
        val TAG = "CategoryFragment"
        val is_update_category = "is_update_category"
        val selected_category = "selected_category"
    }
    private lateinit var viewModel: CategoryViewModel
    private lateinit var binding: CategoryFragmentBinding
    private var isUpdateCategory: Boolean = false
    private var selectedCategory: CategoryEntity? = null
    private var container: ViewGroup? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.container = container
        ActivityWeakMapRef.put(TAG, this);
        injectDagger()
        if ( this::binding.isInitialized == false ) {
            binding = CategoryFragmentBinding.inflate(layoutInflater, container, false)
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
        if( requireArguments().get("is_update_category") !=null)
            isUpdateCategory = requireArguments().getBoolean("is_update_category", false)
        selectedCategory = requireArguments().getParcelable("selected_category") as? CategoryEntity
    }

    private fun createViewModel() {
        if ( !this::viewModel.isInitialized)
            viewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        viewModel.clear()
        binding.viewModel = viewModel
        viewModel.selectedCategory = selectedCategory
        viewModel.isUpdateCategory = isUpdateCategory
        viewModel.categoryName = selectedCategory?.categoryName
        selectedCategory?.categoryDesc?.let {
            viewModel.categoryDesc = it
        }
    }

    private fun setBinding() {
        if ( this::binding.isInitialized == false )
            binding = CategoryFragmentBinding.inflate(layoutInflater, container, false)
        binding.viewModel = viewModel
        binding.category = selectedCategory
        binding.lifecycleOwner = this
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun observeViewModel() {
        viewModel.isCategoryNameEmpty.observe(this, Observer {
            if (!isDetached) {
                if (it) binding.etCategoryName.error = getString(R.string.Please_enter_the_category_name)
            }
        })

        viewModel.isCategoryPriceEmpty.observe(this, Observer {
            if (!isDetached) {
                if (it) binding.etCategoryDesc.error = getString(R.string.Please_enter_the_category_desc)
            }
        })

        viewModel.shouldFinishActivity.observe(this, Observer {
            if (it && !isDetached) {
                Handler(Looper.getMainLooper()).postDelayed({
                    var mainActivity = ActivityWeakMapRef.get(MainActivity.TAG) as MainActivity
                    val info = "Category has been " + (if ( isUpdateCategory ) "Updated" else "Inserted")
                    var toast = Toast.makeText(activity?.baseContext,
                            Html.fromHtml("<font color='red' ><b>" + info + "</b></font>", Html.FROM_HTML_MODE_LEGACY), Toast.LENGTH_LONG)
                    toast.show()

//                    mainActivity.selectItem(MainActivity.CATEGORY_LIST_MENU_NDX)
                    Log.d(CategoryListFragment.TAG, "Re-Routing to MainActivity")
                }, 200)
            }
        })
    }
}