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
import com.sample.libraryapplication.utils.ActivityWeakMapRef
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = MainActivity::class.java.simpleName
    }
    @Inject
    lateinit var bookClickHandlers: BookClickHandlers
    private  lateinit var binding: ActivityBookListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityWeakMapRef.weakMap.put(TAG, this);
        injectDagger()
        setBinding()

        if (savedInstanceState == null) {
            var fragment = supportFragmentManager.findFragmentByTag(BookListFragment.TAG)?: BookListFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment, BookListFragment.TAG)
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
        val drawerItem= mutableListOf<MenuItemDataModel>()

        drawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array)

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
        var tag: String? = null
        when (position) {
            0 -> {
                if (supportFragmentManager.findFragmentByTag(BookListFragment.TAG) != null)
                    Log.d(TAG, "selectItem: no need to create fragment; reusing cached verion")
                    fragment = supportFragmentManager.findFragmentByTag(BookListFragment.TAG)?: BookListFragment()
                    tag = BookListFragment.TAG
                }
            1 -> {
                binding.leftDrawer.setItemChecked(position, true)
                binding.leftDrawer.setSelection(position)
                binding.drawerLayout.closeDrawer(binding.leftDrawer)
                setTitle(drawerItemTitles[position])
                    bookClickHandlers.onFABClicked2(this)
                }
            2 -> {
                if (supportFragmentManager.findFragmentByTag(MQTTFragment.TAG) != null)
                    Log.d(TAG, "selectItem: no need to create fragment; reusing cached verion")
                fragment = supportFragmentManager.findFragmentByTag(MQTTFragment.TAG)
                    ?: MQTTFragment.newInstance()
                tag = MQTTFragment.TAG
            }
        }
        if (fragment != null) {
            var transactionId = supportFragmentManager.beginTransaction()
                                                        .replace(R.id.content_frame, fragment, tag)
                                                        .addToBackStack(null).
                                                        commit()
                                        //            var pendingTX = supportFragmentManager.executePendingTransactions()
            Log.d(TAG, "selectItem: transactionId = $transactionId ")//pendingTX = $pendingTX")
            binding.leftDrawer.setItemChecked(position, true)
            binding.leftDrawer.setSelection(position)
            binding.drawerLayout.closeDrawer(binding.leftDrawer)
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