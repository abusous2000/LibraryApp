package com.sample.libraryapplication.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sample.libraryapplication.R
import com.sample.libraryapplication.utils.ActivityWeakMapRef
import com.sample.libraryapplication.utils.showColoredToast
import dagger.hilt.android.AndroidEntryPoint

data class MenuItemDataModel(var icon: Int, var name: String)

/**
 * An activity that inflates a layout that has a [BottomNavigationView].
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
        companion object {
            val TAG = MainActivity::class.java.simpleName
            val BOOK_LIST_MENU_NDX = 0
            val CATEGORY_LIST_MENU_NDX = 1
            val MQTT_SETTINGS_MENU_NDX = 2
            val QUITE_APP_MENU_NDX = 3
        }
    var currentNavController: LiveData<NavController>? = null

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
            showColoredToast("Double back press to exist")
            this.doubleBackToExitPressedOnce = true
            Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 1000)
        }
        else
            super.onBackPressed()
     }
}
