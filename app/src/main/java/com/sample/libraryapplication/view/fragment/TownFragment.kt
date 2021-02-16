package com.sample.libraryapplication.view.fragment

import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.PalestineRemembered.dataobject.GPTown
import com.PalestineRemembered.dataobject.School
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sample.libraryapplication.R
import com.sample.libraryapplication.utils.CommonUtils
import com.sample.libraryapplication.viewmodel.TownViewModel
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


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
        viewPager2.setCurrentItem(2, true)
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
        Log.d("StatsAndFactsFragment", "onCreateView: "+resources.getString(R.string.Books))
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
        CommonUtils.initWebView(webView, this)

        val jsonfile: String = requireContext().assets.open("Acre/al-Bassa-GPTown.json").bufferedReader().use {it.readText()}
        val geoPointDataJSONObject = JSONObject(jsonfile)

        val gpTownJSON = geoPointDataJSONObject.getJSONObject("GEOPointData").getJSONObject("GPTown")
        val gson = Gson()
        val gpTownSchoolJSON = gpTownJSON.getJSONArray("school")
        val gpTownPopulationJSON = gpTownJSON.getJSONArray("population")
        val itemType = object : TypeToken<List<School>>() {}.type
        val schools = gson.fromJson<List<School>>(gpTownSchoolJSON.toString(), itemType)
        val gsonObj = gson.fromJson(gpTownJSON.toString(), GPTown::class.java)
        var gp = GPTown()
        gp.townTodayArabic = gsonObj.townTodayArabic//gpTownJSON?.getString("townTodayArabic")
        gp.school = schools.toTypedArray()
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

    val view = inflater.inflate(R.layout.fragment_oral_history, container, false)
    val table = view.findViewById<TableLayout>(R.id.stats_and_facts)//TableLayout(requireContext())
    table.isStretchAllColumns = true
    table.isShrinkAllColumns = true
    val rowTitle = TableRow(requireContext())
    rowTitle.setGravity(Gravity.CENTER_HORIZONTAL)
    val rowDayLabels = TableRow(requireContext())
    val rowHighs = TableRow(requireContext())
    val rowLows = TableRow(requireContext())
    val rowConditions = TableRow(requireContext())
    rowConditions.setGravity(Gravity.CENTER)
    val empty = TextView(requireContext())

    // title column/row
    val title = TextView(requireContext())
    title.text = "Java Weather Table"
    title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f)
    title.gravity = Gravity.CENTER
    title.setTypeface(Typeface.SERIF, Typeface.BOLD)
    val params: TableRow.LayoutParams = TableRow.LayoutParams()
    params.span = 6
    rowTitle.addView(title, params)

    // labels column
    val highsLabel = TextView(requireContext())
    highsLabel.text = "Day High"
    highsLabel.setTypeface(Typeface.DEFAULT_BOLD)
    val lowsLabel = TextView(requireContext())
    lowsLabel.text = "Day Low"
    lowsLabel.setTypeface(Typeface.DEFAULT_BOLD)
    val conditionsLabel = TextView(requireContext())
    conditionsLabel.text = "Conditions"
    conditionsLabel.setTypeface(Typeface.DEFAULT_BOLD)
    rowDayLabels.addView(empty)
    rowHighs.addView(highsLabel)
    rowLows.addView(lowsLabel)
    rowConditions.addView(conditionsLabel)

    // day 1 column
    val day1Label = TextView(requireContext())
    day1Label.text = "Feb 7"
    day1Label.setTypeface(Typeface.SERIF, Typeface.BOLD)
    val day1High = TextView(requireContext())
    day1High.text = "28°F"
    day1High.gravity = Gravity.CENTER_HORIZONTAL
    val day1Low = TextView(requireContext())
    day1Low.text = "15°F"
    day1Low.gravity = Gravity.CENTER_HORIZONTAL
    val day1Conditions = ImageView(requireContext())
    day1Conditions.setImageResource(R.drawable.fixtures)
    rowDayLabels.addView(day1Label)
    rowHighs.addView(day1High)
    rowLows.addView(day1Low)
    rowConditions.addView(day1Conditions)

    // day2 column
    val day2Label = TextView(requireContext())
    day2Label.text = "Feb 8"
    day2Label.setTypeface(Typeface.SERIF, Typeface.BOLD)
    val day2High = TextView(requireContext())
    day2High.text = "26°F"
    day2High.gravity = Gravity.CENTER_HORIZONTAL
    val day2Low = TextView(requireContext())
    day2Low.text = "14°F"
    day2Low.gravity = Gravity.CENTER_HORIZONTAL
    val day2Conditions = ImageView(requireContext())
    day2Conditions.setImageResource(R.drawable.ic_drawer)
    rowDayLabels.addView(day2Label)
    rowHighs.addView(day2High)
    rowLows.addView(day2Low)
    rowConditions.addView(day2Conditions)

    // day3 column
    val day3Label = TextView(requireContext())
    day3Label.text = "Feb 9"
    day3Label.setTypeface(Typeface.SERIF, Typeface.BOLD)
    val day3High = TextView(requireContext())
    day3High.text = "23°F"
    day3High.gravity = Gravity.CENTER_HORIZONTAL
    val day3Low = TextView(requireContext())
    day3Low.text = "3°F"
    day3Low.gravity = Gravity.CENTER_HORIZONTAL
    val day3Conditions = ImageView(requireContext())
    day3Conditions.setImageResource(R.drawable.ic_home)
    rowDayLabels.addView(day3Label)
    rowHighs.addView(day3High)
    rowLows.addView(day3Low)
    rowConditions.addView(day3Conditions)

    // day4 column
    val day4Label = TextView(requireContext())
    day4Label.text = "Feb 10"
    day4Label.setTypeface(Typeface.SERIF, Typeface.BOLD)
    val day4High = TextView(requireContext())
    day4High.text = "17°F"
    day4High.gravity = Gravity.CENTER_HORIZONTAL
    val day4Low = TextView(requireContext())
    day4Low.text = "5°F"
    day4Low.gravity = Gravity.CENTER_HORIZONTAL
    val day4Conditions = ImageView(requireContext())
    day4Conditions.setImageResource(R.drawable.ic_news)
    rowDayLabels.addView(day4Label)
    rowHighs.addView(day4High)
    rowLows.addView(day4Low)
    rowConditions.addView(day4Conditions)

    // day5 column
    val day5Label = TextView(requireContext())
    day5Label.text = "Feb 11"
    day5Label.setTypeface(Typeface.SERIF, Typeface.BOLD)
    val day5High = TextView(requireContext())
    day5High.text = "19°F"
    day5High.gravity = Gravity.CENTER_HORIZONTAL
    val day5Low = TextView(requireContext())
    day5Low.text = "6°F"
    day5Low.gravity = Gravity.CENTER_HORIZONTAL
    val day5Conditions = ImageView(requireContext())
    day5Conditions.setImageResource(R.drawable.connect)
    rowDayLabels.addView(day5Label)
    rowHighs.addView(day5High)
    rowLows.addView(day5Low)
    rowConditions.addView(day5Conditions)
    table.addView(rowTitle)
    table.addView(rowDayLabels)
    table.addView(rowHighs)
    table.addView(rowLows)
    table.addView(rowConditions)

//    val configuration: Configuration = resources.configuration
//    configuration.setLayoutDirection(Locale("fr"))
//    requireContext().createConfigurationContext(configuration)
//    resources.updateConfiguration(configuration, resources.displayMetrics)
    val lang = "ar"
    val localeNew = Locale(lang)
    Locale.setDefault(localeNew)

    val res: Resources = requireContext().getResources()
    val newConfig = Configuration(res.getConfiguration())
    newConfig.setLocale(localeNew)
    newConfig.setLayoutDirection(localeNew)
    res.updateConfiguration(newConfig,res.getDisplayMetrics())

    Log.d("OH", "onCreateView: "+resources.getString(R.string.Books))
    return view
//    setContentView(table)
}
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
//                             ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_oral_history, container, false)
//    }
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