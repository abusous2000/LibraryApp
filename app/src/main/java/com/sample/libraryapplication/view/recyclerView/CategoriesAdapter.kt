package com.sample.libraryapplication.view.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.R
import com.sample.libraryapplication.database.entity.CategoryEntity
import com.sample.libraryapplication.databinding.ListItemRvCategoryBinding
import com.sample.libraryapplication.view.BookClickHandlers
import javax.inject.Inject


class CategoriesAdapter(private var categoryList: List<CategoryEntity>?) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {
    @Inject
    lateinit var bookClickHandler: BookClickHandlers

    init{
        LibraryApplication.instance.libraryComponent.inject(this)
    }
    fun updateCategoryList(newCategoriesList: List<CategoryEntity>?) {
        val diffResult = DiffUtil.calculateDiff(CategoriesDiffCallback(categoryList, newCategoriesList), false)
        categoryList = newCategoriesList
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_item_rv_category, parent, false))
    }

    override fun getItemCount(): Int {
        return categoryList?.size ?: 0
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.dataBinding.setVariable(BR.category, categoryList?.get(position))
        holder.dataBinding.setVariable(BR.clickHandlers, bookClickHandler)
    }

    class CategoryViewHolder(binding: ListItemRvCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        var dataBinding = binding
    }
 }