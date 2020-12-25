package com.sample.libraryapplication.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.R
import com.sample.libraryapplication.databinding.MainActivityBinding
import com.sample.libraryapplication.service.MyMQTTHandler
import com.sample.libraryapplication.utils.ActivityWeakMapRef
import com.sample.libraryapplication.view.fragment.BookListFragment
import com.sample.libraryapplication.view.fragment.CategoryListFragment
import com.sample.libraryapplication.view.fragment.MQTTFragment
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = MainActivity::class.java.simpleName
        val BOOK_LIST_MENU_NDX = 0
        val CATEGORY_LIST_MENU_NDX = 1
        val MQTT_SETTINGS_MENU_NDX = 2
        val QUITE_APP_MENU_NDX = 3
    }

    @Inject
    lateinit var bookClickHandlers: BookClickHandlers

    @Inject
    lateinit var myMQTTHandler: MyMQTTHandler
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityWeakMapRef.put(TAG, this);
        injectDagger()
        setBinding()

        if (savedInstanceState == null) {
            var fragment = supportFragmentManager.findFragmentByTag(BookListFragment.TAG) ?: BookListFragment()
            supportFragmentManager.beginTransaction().replace(R.id.content_frame, fragment, BookListFragment.TAG).addToBackStack(null).commit()
        }
        myMQTTHandler.connect()
    }

    private fun injectDagger() {
        LibraryApplication.instance.libraryComponent.inject(this)
    }

    data class MenuItemDataModel(var icon: Int, var name: String)

    lateinit var drawerItemTitles: Array<String>

    private fun setBinding() {
        binding = MainActivityBinding.inflate(layoutInflater)
        val drawerItem = mutableListOf<MenuItemDataModel>()

        drawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array)

        drawerItem.add(MenuItemDataModel(R.drawable.connect, drawerItemTitles[0]))
        drawerItem.add(MenuItemDataModel(R.drawable.fixtures, drawerItemTitles[1]))
        drawerItem.add(MenuItemDataModel(R.drawable.table, drawerItemTitles[2]))
        drawerItem.add(MenuItemDataModel(R.drawable.ic_baseline_exit_to_app_24, drawerItemTitles[3]))

        val adapter = DrawerItemCustomAdapter(this, R.layout.menu_view_item_row, drawerItem)
        binding.leftDrawer.adapter = adapter
        binding.leftDrawer.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectItem(position)
            }
        }
        var toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.topAppBar, R.string.app_name, R.string.app_name)
        toggle.syncState()
        binding.drawerLayout.addDrawerListener(toggle)
        setContentView(binding.root)
    }

    fun selectItem(position: Int) {
        Log.d(TAG, "onOptionsItemSelected: Item $position was clicked")

        var fragment: Fragment? = null
        var tag: String? = null
        when (position) {
            BOOK_LIST_MENU_NDX -> {
                if (supportFragmentManager.findFragmentByTag(BookListFragment.TAG) != null) {
                    Log.d(TAG, "selectItem: no need to create BookListFragment; re-using cached verion")
                }
                fragment = supportFragmentManager.findFragmentByTag(BookListFragment.TAG) ?: BookListFragment()
                tag = BookListFragment.TAG
            }
            CATEGORY_LIST_MENU_NDX -> {
                if (supportFragmentManager.findFragmentByTag(CategoryListFragment.TAG) != null) {
                    Log.d(TAG, "selectItem: no need to create CategoryListFragment; re-using cached verion")
                }
                fragment = supportFragmentManager.findFragmentByTag(CategoryListFragment.TAG) ?: CategoryListFragment()
                tag = CategoryListFragment.TAG
            }
            MQTT_SETTINGS_MENU_NDX -> {
                if (supportFragmentManager.findFragmentByTag(MQTTFragment.TAG) != null) {
                    Log.d(TAG, "selectItem: no need to create MQTTFragment; re-using cached verion")
                }
                fragment = supportFragmentManager.findFragmentByTag(MQTTFragment.TAG) ?: MQTTFragment.newInstance()
                tag = MQTTFragment.TAG
            }
            QUITE_APP_MENU_NDX -> QuitApp()
        }
        if (fragment != null) {
            val tx = supportFragmentManager.beginTransaction()

            tx.replace(R.id.content_frame, fragment, tag)
            tx.addToBackStack(null)
            tx.commit()
            Log.d(TAG, "selectItem: Serving $tag Fragment") //pendingTX = $pendingTX")
            binding.leftDrawer.setItemChecked(position, true)
            binding.leftDrawer.setSelection(position)
            binding.drawerLayout.closeDrawer(binding.leftDrawer)
            binding.topAppBar.title = getResources().getString(R.string.app_name) + ": " + drawerItemTitles.get(position)
        }
        else {
            Log.e("MainActivity", "No Fragment to serve: $position")
        }
    }

    fun onExit() {
        myMQTTHandler.close()
    }

    fun QuitApp(view: View? = null) {
        onExit()
        android.os.Process.killProcess(android.os.Process.myPid()); }

    override fun onDestroy() {
        super.onDestroy()
        onExit()
    }
}