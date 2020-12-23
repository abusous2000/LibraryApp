package com.sample.libraryapplication.view.recyclerView

import androidx.recyclerview.widget.DiffUtil
import com.sample.libraryapplication.database.entity.CategoryEntity

class CategoriesDiffCallback(private val oldCategoriesList: List<CategoryEntity>?, private val newCategoriesList: List<CategoryEntity>?) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCategoriesList?.get(oldItemPosition)?.id == newCategoriesList?.get(newItemPosition)?.id
    }

    override fun getOldListSize(): Int {
        return oldCategoriesList?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newCategoriesList?.size ?: 0
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCategoriesList?.get(oldItemPosition)?.equals(newCategoriesList?.get(newItemPosition))!!
    }
}