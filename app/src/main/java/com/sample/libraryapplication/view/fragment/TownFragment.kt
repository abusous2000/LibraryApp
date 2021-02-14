package com.sample.libraryapplication.view.fragment

import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.*
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.sample.libraryapplication.R
import com.sample.libraryapplication.utils.CommonUtils
import com.sample.libraryapplication.viewmodel.TownViewModel


class TownFragment : Fragment() {
    companion object {
        fun newInstance() = TownFragment()
    }
    lateinit var tabLayout :TabLayout
    private lateinit var viewModel: TownViewModel
    private lateinit var viewPager2 :ViewPager2
    private val TAG = "TownFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
                             ): View? {
        val view = inflater.inflate(R.layout.town_fragment, container, false)

        tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        viewPager2 = view.findViewById<ViewPager2>(R.id.view_pager)
        viewPager2.isUserInputEnabled = false
//        view.findViewById<TabItem>(R.id.articles)?.visibility = View.GONE

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager2.currentItem = tab!!.position
                Log.d(TAG, "onTabSelected: ${tab?.text}")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })
//        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addTab(tabLayout.newTab().setText("Stats & Facts"));
        tabLayout.addTab(tabLayout.newTab().setText("Pictures"));
        tabLayout.addTab(tabLayout.newTab().setText("Articles"));
        tabLayout.addTab(tabLayout.newTab().setText("Oral History"));
        tabLayout.addTab(tabLayout.newTab().setText("Members"));
        setupViewPager(viewPager2)
        tabLayout.getTabAt(2)?.select()
        viewPager2.setCurrentItem(2,true)
//        viewPager2.adapter = AppViewPagerAdapter(fragmentManager, lifecycle)

//        TabLayoutMediator(tabLayout, viewPager2, object : TabLayoutMediator.OnConfigureTabCallback {
//            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
//                // Styling each tab here
//                tab.text = "Tab $position"
//            }
//        }).attach()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        view.findViewById<TabItem>(R.id.articles)?.visibility = View.GONE
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TownViewModel::class.java)
        // TODO: Use the ViewModel
    }
    private fun setupViewPager(viewPager: ViewPager2) {
        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle)

        adapter.addFragment(StatsAndFactsFragment(), "Stats & Facts")
        adapter.addFragment(PicturesFragment(), "Pictures")
        adapter.addFragment(ArticlesFragment(), "Articles")
        adapter.addFragment(OralHistoryFragment(), "Oral History")
        adapter.addFragment(MembersFragment(), "Members")

        viewPager.adapter = adapter
    }

}

internal class ViewPagerAdapter(manager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(manager, lifecycle) {
    private val mFragmentList: MutableList<Fragment> = ArrayList()
    private val mFragmentTitleList: MutableList<String> = ArrayList()
    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }
}

internal class StatsAndFactsFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
                             ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats_and_facts, container, false)
    }
}

internal class PicturesFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
                             ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pictures, container, false)
    }
}

internal class ArticlesFragment : Fragment() {
    lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
                             ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_articles, container, false)
        webView = view.findViewById<WebView>(R.id.webview)
        CommonUtils.initWebView(webView,this)
        webView.loadUrl("https://www.palestineremembered.com/MissionStatement.htm");

        return view
    }
//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_BACK && this.webView.canGoBack()) {
//            this.webView.goBack()
//            return true
//        }
//        return super.onKeyDown(keyCode, event)
//    }


}

internal class OralHistoryFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
                             ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_oral_history, container, false)
    }
}

internal class MembersFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
                             ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_members, container, false)
    }
}