package com.sample.libraryapplication.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sample.libraryapplication.R
import com.sample.libraryapplication.utils.ActivityWeakMapRef

data class MenuItemDataModel(var icon: Int, var name: String)
//class MainActivity2 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
//    companion object {
//        val TAG = MainActivity::class.java.simpleName
//        val BOOK_LIST_MENU_NDX = 0
//        val CATEGORY_LIST_MENU_NDX = 1
//        val MQTT_SETTINGS_MENU_NDX = 2
//        val QUITE_APP_MENU_NDX = 3
//    }
//
//    @Inject
//    lateinit var bookClickHandlers: BookClickHandlers
//
//    @Inject
//    lateinit var myMQTTHandler: MyMQTTHandler
//    private lateinit var binding: MainActivityBinding
//    lateinit var bottomNav: BottomNavigationView
//    lateinit var navController: NavController
//    lateinit var drawerLayout: DrawerLayout
//    private lateinit var appBarConfiguration: AppBarConfiguration
//    var cnt = 0
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        //        setContentView(R.layout.main_activity)
//        ActivityWeakMapRef.put(TAG, this);
//        injectDagger()
//        //        setBinding()
//        binding = MainActivityBinding.inflate(layoutInflater)
//        bottomNav = binding.bottomNavigation
//        drawerLayout = binding.drawerLayout
//        setContentView(binding.root)
//        val navHostFragment =  supportFragmentManager.findFragmentById(R.id.hostFragment) as NavHostFragment
////        navController = findNavController(R.id.hostFragment)
//        navController = navHostFragment.navController
//        // get fragment
////        val navHostFragment = supportFragmentManager.findFragmentById(R.id.hostFragment)!!
//
//        // set navigation graph
////        navController.setGraph(R.navigation.app_navigation)
//
//        navController.addOnDestinationChangedListener({ _, destination, _ ->
//            Log.d(TAG, "onCreate: :"+destination.id)
////            supportActionBar?.title = destination.id.toString() + ":" + (++cnt)
////            navController.currentDestination?.label = destination.id.toString() + ":" + (++cnt)
////            destination.label = destination.id.toString()
//  //            binding.topAppBar.title = getResources().getString(R.string.app_name) + ": " + drawerItemTitles.get(position)
//        })
////        binding.navigationView.setNavigationItemSelectedListener(this)
//        setupBottomNavigation()
//        appBarConfiguration = AppBarConfiguration(navController.graph,drawerLayout)
//        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout)
//
//        NavigationUI.setupWithNavController(navigation_view,navController)
//        //        if (savedInstanceState == null) {
//        //            var fragment = supportFragmentManager.findFragmentByTag(BookListFragment.TAG) ?: BookListFragment()
//        //            supportFragmentManager.beginTransaction().replace(R.id.content_frame, fragment, BookListFragment.TAG).addToBackStack(null).commit()
//        //        }
//        myMQTTHandler.connect()
////        setLayoutDirection(binding.,ViewCompat.LAYOUT_DIRECTION_RTL)
//
////        while(navHostFragment.childFragmentManager.getBackStackEntryCount() > 0) { navHostFragment.childFragmentManager.popBackStackImmediate(); }
//
//
//    }
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
//////        setContentView(R.layout.main_activity)
////        ActivityWeakMapRef.put(TAG, this);
////        injectDagger()
//////        setBinding()
////        binding = MainActivityBinding.inflate(layoutInflater)
////        bottomNav = binding.bottomNavigation
////        drawerLayout = binding.drawerLayout
////        setContentView(binding.root)
////        navController = findNavController(R.id.hostFragment)
////        navController.addOnDestinationChangedListener({ _, destination, _ ->
////            Log.d(TAG, "onCreate: :"+destination.id)
////        }
////
////                                                     )
////        binding.navigationView.setNavigationItemSelectedListener(this)
////        setupBottomNavigation()
////        appBarConfiguration = AppBarConfiguration(navController.graph,drawerLayout)
////        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout)
////
////        NavigationUI.setupWithNavController(navigation_view,navController)
//////        if (savedInstanceState == null) {
//////            var fragment = supportFragmentManager.findFragmentByTag(BookListFragment.TAG) ?: BookListFragment()
//////            supportFragmentManager.beginTransaction().replace(R.id.content_frame, fragment, BookListFragment.TAG).addToBackStack(null).commit()
//////        }
////        myMQTTHandler.connect()
////
////    }
////    private var doubleBackToExitPressedOnce = false
////    override fun onBackPressed() {
////        if (doubleBackToExitPressedOnce) {
////            super.onBackPressed()
////            return
////        }
////
////        this.doubleBackToExitPressedOnce = true
//////        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
////
////        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 1000)
////    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        //return navController.navigateUp()
//        return NavigationUI.navigateUp(navController,appBarConfiguration)
//
//    }
//
//    private fun setupBottomNavigation() {
//        bottomNav.setupWithNavController(navController)
//    }
//
//    private fun injectDagger() {
//        LibraryApplication.instance.libraryComponent.inject(this)
//    }
//
//
//
//    lateinit var drawerItemTitles: Array<String>
//
////    private fun setBinding() {
////        binding = MainActivityBinding.inflate(layoutInflater)
////        val drawerItem = mutableListOf<MenuItemDataModel>()
////
////        drawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array)
////
////        drawerItem.add(MenuItemDataModel(R.drawable.connect, drawerItemTitles[0]))
////        drawerItem.add(MenuItemDataModel(R.drawable.fixtures, drawerItemTitles[1]))
////        drawerItem.add(MenuItemDataModel(R.drawable.table, drawerItemTitles[2]))
////        drawerItem.add(MenuItemDataModel(R.drawable.ic_baseline_exit_to_app_24, drawerItemTitles[3]))
////
////        val adapter = DrawerItemCustomAdapter(this, R.layout.menu_view_item_row, drawerItem)
////        binding.leftDrawer.adapter = adapter
////        binding.leftDrawer.onItemClickListener = object : AdapterView.OnItemClickListener {
////            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
////                selectItem(position)
////            }
////        }
////        var toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.topAppBar, R.string.app_name, R.string.app_name)
////        toggle.syncState()
////        binding.drawerLayout.addDrawerListener(toggle)
////        setContentView(binding.root)
////    }
////    override fun onOptionsItemSelected(item: MenuItem): Boolean {
////        Log.d(TAG, "onOptionsItemSelected: "+ item.itemId)
////
////        super.onOptionsItemSelected(item)
////        return true
////    }
//
//
//    fun selectItem(position: Int) {
//        Log.d(TAG, "onOptionsItemSelected: Item $position was clicked")
//
//        var fragment: Fragment? = null
//        var tag: String? = null
//        when (position) {
//            BOOK_LIST_MENU_NDX -> {
//                if (supportFragmentManager.findFragmentByTag(BookListFragment.TAG) != null) {
//                    Log.d(TAG, "selectItem: no need to create BookListFragment; re-using cached verion")
//                }
//                fragment = supportFragmentManager.findFragmentByTag(BookListFragment.TAG) ?: BookListFragment()
//                tag = BookListFragment.TAG
//            }
//            CATEGORY_LIST_MENU_NDX -> {
//                if (supportFragmentManager.findFragmentByTag(CategoryListFragment.TAG) != null) {
//                    Log.d(TAG, "selectItem: no need to create CategoryListFragment; re-using cached verion")
//                }
//                fragment = supportFragmentManager.findFragmentByTag(CategoryListFragment.TAG) ?: CategoryListFragment()
//                tag = CategoryListFragment.TAG
//            }
//            MQTT_SETTINGS_MENU_NDX -> {
//                if (supportFragmentManager.findFragmentByTag(MQTTFragment.TAG) != null) {
//                    Log.d(TAG, "selectItem: no need to create MQTTFragment; re-using cached verion")
//                }
//                fragment = supportFragmentManager.findFragmentByTag(MQTTFragment.TAG) ?: MQTTFragment.newInstance()
//                tag = MQTTFragment.TAG
//            }
//            QUITE_APP_MENU_NDX -> QuitApp()
//        }
////        if (fragment != null) {
////            val tx = supportFragmentManager.beginTransaction()
////
////            tx.replace(R.id.content_frame, fragment, tag)
////            tx.addToBackStack(null)
////            tx.commit()
////            Log.d(TAG, "selectItem: Serving $tag Fragment") //pendingTX = $pendingTX")
////            binding.leftDrawer.setItemChecked(position, true)
////            binding.leftDrawer.setSelection(position)
////            binding.drawerLayout.closeDrawer(binding.leftDrawer)
////            binding.topAppBar.title = getResources().getString(R.string.app_name) + ": " + drawerItemTitles.get(position)
////        }
////        else {
////            Log.e("MainActivity", "No Fragment to serve: $position")
////        }
//    }
//
//    fun onExit() {
//        myMQTTHandler.close()
//    }
//
//    fun QuitApp() {
//        onExit()
//        android.os.Process.killProcess(android.os.Process.myPid()); }
//
//     override fun onDestroy() {
//        super.onDestroy()
//        onExit()
//    }
//
//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        Log.d(TAG, "onNavigationItemSelected: "+ item.itemId)
//
//        return true
//    }
//
//}
//



/**
 * An activity that inflates a layout that has a [BottomNavigationView].
 */
class MainActivity : AppCompatActivity() {
        companion object {
            val TAG = MainActivity::class.java.simpleName
            val BOOK_LIST_MENU_NDX = 0
            val CATEGORY_LIST_MENU_NDX = 1
            val MQTT_SETTINGS_MENU_NDX = 2
            val QUITE_APP_MENU_NDX = 3
        }
    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
                ActivityWeakMapRef.put(TAG, this);
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        val navGraphIds = listOf(R.navigation.book_list_nav, R.navigation.category_list_nav, R.navigation.mqtt_nav)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = supportFragmentManager,
                containerId = R.id.nav_host_container,
                intent = intent)

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })
        currentNavController = controller

//        bottomNavigationView.menu.findItem(R.id.mqtt_nav).isEnabled = false
//        bottomNavigationView.selectedItemId =R.id.mqtt_nav
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
        private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        val backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount()
        if ( backStackEntryCount == 0 ){
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }

            this.doubleBackToExitPressedOnce = true
            //        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

            Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 1000)
        }
        else
            super.onBackPressed()
     }
}
