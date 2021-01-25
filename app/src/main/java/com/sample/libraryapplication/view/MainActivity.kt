package com.sample.libraryapplication.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.plusAssign
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.sample.libraryapplication.LibraryApplication
import com.sample.libraryapplication.R
import com.sample.libraryapplication.databinding.MainActivityBinding
import com.sample.libraryapplication.service.MyMQTTHandler
import com.sample.libraryapplication.utils.ActivityWeakMapRef
import com.sample.libraryapplication.view.fragment.BookListFragment
import com.sample.libraryapplication.view.fragment.CategoryListFragment
import com.sample.libraryapplication.view.fragment.MQTTFragment
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

data class MenuItemDataModel(var icon: Int, var name: String)
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
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
    lateinit var bottomNav: BottomNavigationView
    lateinit var navController: NavController
    lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    var cnt = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        setContentView(R.layout.main_activity)
        ActivityWeakMapRef.put(TAG, this);
        injectDagger()
        //        setBinding()
        binding = MainActivityBinding.inflate(layoutInflater)
        bottomNav = binding.bottomNavigation
        drawerLayout = binding.drawerLayout
        setContentView(binding.root)

        navController = findNavController(R.id.hostFragment)

        // get fragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.hostFragment)!!

        // set navigation graph
        navController.setGraph(R.navigation.app_navigation)

        navController.addOnDestinationChangedListener({ _, destination, _ ->
            Log.d(TAG, "onCreate: :"+destination.id)
//            supportActionBar?.title = destination.id.toString() + ":" + (++cnt)
//            navController.currentDestination?.label = destination.id.toString() + ":" + (++cnt)
//            destination.label = destination.id.toString()
  //            binding.topAppBar.title = getResources().getString(R.string.app_name) + ": " + drawerItemTitles.get(position)
        })
//        binding.navigationView.setNavigationItemSelectedListener(this)
        setupBottomNavigation()
        appBarConfiguration = AppBarConfiguration(navController.graph,drawerLayout)
        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout)

        NavigationUI.setupWithNavController(navigation_view,navController)
        //        if (savedInstanceState == null) {
        //            var fragment = supportFragmentManager.findFragmentByTag(BookListFragment.TAG) ?: BookListFragment()
        //            supportFragmentManager.beginTransaction().replace(R.id.content_frame, fragment, BookListFragment.TAG).addToBackStack(null).commit()
        //        }
        myMQTTHandler.connect()

//        while(navHostFragment.childFragmentManager.getBackStackEntryCount() > 0) { navHostFragment.childFragmentManager.popBackStackImmediate(); }


    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        setContentView(R.layout.main_activity)
//        ActivityWeakMapRef.put(TAG, this);
//        injectDagger()
////        setBinding()
//        binding = MainActivityBinding.inflate(layoutInflater)
//        bottomNav = binding.bottomNavigation
//        drawerLayout = binding.drawerLayout
//        setContentView(binding.root)
//        navController = findNavController(R.id.hostFragment)
//        navController.addOnDestinationChangedListener({ _, destination, _ ->
//            Log.d(TAG, "onCreate: :"+destination.id)
//        }
//
//                                                     )
//        binding.navigationView.setNavigationItemSelectedListener(this)
//        setupBottomNavigation()
//        appBarConfiguration = AppBarConfiguration(navController.graph,drawerLayout)
//        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout)
//
//        NavigationUI.setupWithNavController(navigation_view,navController)
////        if (savedInstanceState == null) {
////            var fragment = supportFragmentManager.findFragmentByTag(BookListFragment.TAG) ?: BookListFragment()
////            supportFragmentManager.beginTransaction().replace(R.id.content_frame, fragment, BookListFragment.TAG).addToBackStack(null).commit()
////        }
//        myMQTTHandler.connect()
//
//    }
//    private var doubleBackToExitPressedOnce = false
//    override fun onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed()
//            return
//        }
//
//        this.doubleBackToExitPressedOnce = true
////        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
//
//        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 1000)
//    }

    override fun onSupportNavigateUp(): Boolean {
        //return navController.navigateUp()
        return NavigationUI.navigateUp(navController,appBarConfiguration)

    }

    private fun setupBottomNavigation() {
        bottomNav.setupWithNavController(navController)
    }

    private fun injectDagger() {
        LibraryApplication.instance.libraryComponent.inject(this)
    }



    lateinit var drawerItemTitles: Array<String>

//    private fun setBinding() {
//        binding = MainActivityBinding.inflate(layoutInflater)
//        val drawerItem = mutableListOf<MenuItemDataModel>()
//
//        drawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array)
//
//        drawerItem.add(MenuItemDataModel(R.drawable.connect, drawerItemTitles[0]))
//        drawerItem.add(MenuItemDataModel(R.drawable.fixtures, drawerItemTitles[1]))
//        drawerItem.add(MenuItemDataModel(R.drawable.table, drawerItemTitles[2]))
//        drawerItem.add(MenuItemDataModel(R.drawable.ic_baseline_exit_to_app_24, drawerItemTitles[3]))
//
//        val adapter = DrawerItemCustomAdapter(this, R.layout.menu_view_item_row, drawerItem)
//        binding.leftDrawer.adapter = adapter
//        binding.leftDrawer.onItemClickListener = object : AdapterView.OnItemClickListener {
//            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                selectItem(position)
//            }
//        }
//        var toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.topAppBar, R.string.app_name, R.string.app_name)
//        toggle.syncState()
//        binding.drawerLayout.addDrawerListener(toggle)
//        setContentView(binding.root)
//    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        Log.d(TAG, "onOptionsItemSelected: "+ item.itemId)
//
//        super.onOptionsItemSelected(item)
//        return true
//    }


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
//        if (fragment != null) {
//            val tx = supportFragmentManager.beginTransaction()
//
//            tx.replace(R.id.content_frame, fragment, tag)
//            tx.addToBackStack(null)
//            tx.commit()
//            Log.d(TAG, "selectItem: Serving $tag Fragment") //pendingTX = $pendingTX")
//            binding.leftDrawer.setItemChecked(position, true)
//            binding.leftDrawer.setSelection(position)
//            binding.drawerLayout.closeDrawer(binding.leftDrawer)
//            binding.topAppBar.title = getResources().getString(R.string.app_name) + ": " + drawerItemTitles.get(position)
//        }
//        else {
//            Log.e("MainActivity", "No Fragment to serve: $position")
//        }
    }

    fun onExit() {
        myMQTTHandler.close()
    }

    fun QuitApp() {
        onExit()
        android.os.Process.killProcess(android.os.Process.myPid()); }

     override fun onDestroy() {
        super.onDestroy()
        onExit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onNavigationItemSelected: "+ item.itemId)

        return true
    }

}