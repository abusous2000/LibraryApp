package com.sample.libraryapplication.view


import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.sample.libraryapplication.R
import com.sample.libraryapplication.database.entity.BookEntity
import com.sample.libraryapplication.database.entity.CategoryEntity
import com.sample.libraryapplication.utils.ActivityWeakMapRef
import com.sample.libraryapplication.utils.GlideApp
import com.sample.libraryapplication.view.fragment.BookFragment
import com.sample.libraryapplication.view.fragment.BookListFragment
import com.sample.libraryapplication.view.fragment.CategoryFragment
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BookClickHandlers @Inject constructor(): PopupMenu.OnMenuItemClickListener,
    Toolbar.OnMenuItemClickListener {
    companion object {
        val TAG = BookClickHandlers::class.java.simpleName
    }
    private var selectedCategory: CategoryEntity? = null
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return this.onPopMenuClicked(item)
    }
    fun onCategoryItemClicked(view: View?, category: CategoryEntity) {
        val mainActivity = ActivityWeakMapRef.get(MainActivity.TAG) as MainActivity
        val categoryFragment= mainActivity.supportFragmentManager.findFragmentByTag(CategoryFragment.TAG)?:CategoryFragment()

        with(categoryFragment){
            if ( arguments == null )
                arguments = Bundle()
            requireArguments().clear()
            requireArguments().putBoolean(CategoryFragment.is_update_category, category.id != null)
            requireArguments().putParcelable(CategoryFragment.selected_category, category)
        }

        Log.d(TAG, "selectItem: Serving BookFragment for update")
    }
    fun onBookItemClicked(view: View?, book: BookEntity) {
        val mainActivity = ActivityWeakMapRef.get(MainActivity.TAG) as MainActivity
        val arguments = Bundle()
        arguments.putBoolean(BookFragment.is_update_book, book.id != null)
        arguments.putParcelable(BookFragment.selected_book, book)
        arguments.putLong(BookFragment.selected_category_id, book.bookCategoryID!!)
        arguments.putString("dynamicTitle","My Book Label")
        mainActivity.currentNavController?.value?.navigate(R.id.action_bookListFragment_to_bookFragment, arguments)

    }
    fun onShowTown(view: View?) {
        val mainActivity = ActivityWeakMapRef.get(MainActivity.TAG) as MainActivity

        mainActivity.currentNavController?.value?.navigate(R.id.action_bookListFragment_to_townFragment)

    }
    fun onFABClicked(view: View) {
        val popup = PopupMenu(view.context, view)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()

    }
    fun onFABClicked2(view: Activity) {
        var newBook = BookEntity()

        newBook.bookCategoryID =selectedCategory?.id
        onBookItemClicked(null, newBook)
    }

    fun onPopMenuClicked(menuItem: MenuItem?): Boolean {
        var mainActivity = ActivityWeakMapRef.get(MainActivity.TAG) as MainActivity
        when(menuItem?.itemId){
            R.id.Add_A_Book_id -> onFABClicked2(mainActivity)
            R.id.Add_A_Category_id -> onCategoryItemClicked(null, CategoryEntity())
            else -> Toast.makeText(mainActivity, "Item ${menuItem?.itemId} was clicked", Toast.LENGTH_SHORT).show();
        }
        return true;
   }

    fun onCategorySelected(
        @Suppress("UNUSED_PARAMETER") parent: AdapterView<*>?, @Suppress("UNUSED_PARAMETER") view: View?, position: Int, @Suppress("UNUSED_PARAMETER") id: Long
                          ) {
        Log.d(BookClickHandlers.TAG, "onCategorySelected:$position")
        var bookListFragment = ActivityWeakMapRef.get(BookListFragment.TAG) as BookListFragment
        if ( bookListFragment != null ){
            //Check if the list was populated. It could be empty on startup since the DB takes longer to populate
            if ( bookListFragment.boCategory.categories.value?.size!! > 0 ) {
                     selectedCategory = bookListFragment.boCategory.categories.value!!.get(position)
            }
            if ( selectedCategory != null )
                bookListFragment.updateRVBookList(selectedCategory!!)
        }
     }
}

@BindingAdapter("glideCropCenter")
fun setProgress(view: ImageView, url: String?) {
    GlideApp.with(view.getContext()).load(url).centerCrop().into(view)
}

@BindingAdapter("glideExactDiminisions")
fun setWithExactDiminsions(view: ImageView, url: String?) {
    val target = object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                view.setImageBitmap(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                // this is called when imageView is cleared on lifecycle call or for
                // some other reason.
                // if you are referencing the bitmap somewhere else too other than this imageView
                // clear it here as you can no longer have the bitmap
            }
        }
    GlideApp.with(view.getContext()).asBitmap().load(url).centerCrop().override(Target.SIZE_ORIGINAL).into(target)
}
