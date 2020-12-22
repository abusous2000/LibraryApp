package com.sample.libraryapplication.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.R
import com.sample.libraryapplication.databinding.ActivityBookListBinding
import com.sample.libraryapplication.viewmodel.BookListViewModel
import javax.inject.Inject


class BookListActivity : AppCompatActivity() {
    companion object {
        val TAG = BookListActivity::class.java.simpleName
    }
    @Inject
    lateinit var bookClickHandlers: BookClickHandlers
    private  lateinit var binding: ActivityBookListBinding
    lateinit var bookListViewModel: BookListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityWeakRef.updateActivity(TAG, this);
        injectDagger()
        setBinding()

        if (savedInstanceState == null) {
            var fragment = supportFragmentManager.findFragmentByTag(MainFragment.TAG)?: MainFragment()
            var transactionId = supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment, MainFragment.TAG)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun injectDagger() {
        LibraryApplication.instance.libraryComponent.inject(this)
    }

    data class MenuItemDataModel(var icon: Int, var name: String)
    lateinit var drawerItemTitles: Array<String>

    private fun setBinding() {
        binding = ActivityBookListBinding.inflate(layoutInflater)
//        binding.lifecycleOwner = this
        val drawerItem= mutableListOf<MenuItemDataModel>()

        drawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        drawerItem.add(MenuItemDataModel(R.drawable.connect,    drawerItemTitles[0]))
        drawerItem.add(MenuItemDataModel(R.drawable.fixtures,   drawerItemTitles[1]))
        drawerItem.add(MenuItemDataModel(R.drawable.table,      drawerItemTitles[2]))

        val adapter = DrawerItemCustomAdapter(this, R.layout.menu_view_item_row, drawerItem)
        binding.leftDrawer.adapter =adapter
        binding.leftDrawer.onItemClickListener =  object: AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectItem(position)
            }
        }
        var toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.topAppBar,R.string.app_name,R.string.app_name )
        toggle.syncState()
        binding.drawerLayout.addDrawerListener(toggle)
        setContentView(binding.root)
    }
    private fun selectItem(position: Int) {
        Log.d(TAG, "onOptionsItemSelected: Item $position was clicked")

        var fragment: Fragment? = null
        when (position) {
            0, 1 -> {
                if (position == 0) {
                    bookClickHandlers.onFABClicked3(this)
                }
                if (position == 1) {
                    bookClickHandlers.onFABClicked2(this)
                }
                binding.leftDrawer.setItemChecked(position, true)
                binding.leftDrawer.setSelection(position)
                binding.drawerLayout.closeDrawer(binding.leftDrawer)
                setTitle(drawerItemTitles[position])
            }
            2 -> {
                fragment = supportFragmentManager.findFragmentByTag(drawerItemTitles[position])
                    ?: MainFragment()//MQTTFragment.newInstance()
            }
        }
        if (fragment != null) {
            var transactionId = supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment, drawerItemTitles[position])
                .addToBackStack(null).commit()
//            var pendingTX = supportFragmentManager.executePendingTransactions()
            Log.d(TAG, "selectItem: transactionId = $transactionId ")//pendingTX = $pendingTX")
            binding.leftDrawer.setItemChecked(position, true)
            binding.leftDrawer.setSelection(position)
            binding.drawerLayout.closeDrawer(binding.leftDrawer)
//            setTitle(drawerItemTitles[position])
        } else {
            Log.e("MainActivity", "No Fragment to serve: $position")
        }
    }
    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}