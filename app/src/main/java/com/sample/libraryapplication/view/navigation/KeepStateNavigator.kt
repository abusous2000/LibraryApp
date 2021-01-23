package com.sample.libraryapplication.view.navigation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import com.sample.libraryapplication.R
import com.sample.libraryapplication.utils.ActivityWeakMapRef
import com.sample.libraryapplication.view.MainActivity
import com.sample.libraryapplication.view.fragment.BookListFragment


@Navigator.Name("keep_state_fragment") // `keep_state_fragment` is used in navigation xml
class KeepStateNavigator(
    private val context: Context, private val manager: FragmentManager, // Should pass childFragmentManager.
    private val containerId: Int
                        ) : FragmentNavigator(context, manager, containerId) {

    var backStackIndex = 0

    private fun genBackStackName(destId: Int): String? {
        return "$backStackIndex-$destId"
    }

    override fun navigate(
        destination: Destination, args: Bundle?, navOptions: NavOptions?, navigatorExtras: Navigator.Extras?): NavDestination? {
        val tag = destination.id.toString()
        val transaction = manager.beginTransaction()

        var initialNavigate = false
        val currentFragment = manager.primaryNavigationFragment
        if (currentFragment != null) {
            transaction.detach(currentFragment)
        } else {
            initialNavigate = true
        }

        var fragment = manager.findFragmentByTag(tag)
        if (fragment == null) {
            val className = destination.className
            fragment = manager.fragmentFactory.instantiate(context.classLoader, className)
            transaction.add(containerId, fragment, tag)
        } else {
            transaction.attach(fragment)
        }
        fragment.arguments =args

        transaction.setPrimaryNavigationFragment(fragment)
        transaction.setReorderingAllowed(true)

        if ( navOptions!= null && navOptions.isPopUpToInclusive() && navOptions.shouldLaunchSingleTop() ) {
            backStackIndex = 0
            while (popBackStack());
        }
        else
        if ( backStackIndex != 0 )
            transaction.addToBackStack(genBackStackName(destination.id))
        backStackIndex++
        transaction.commit()
        return if (initialNavigate) {

            destination
        } else {
            null
        }
    }
}