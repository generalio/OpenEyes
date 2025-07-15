package com.generals.module.home.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.viewpager2.widget.ViewPager2
import com.generals.module.home.R
import com.generals.module.home.ui.activity.HomeActivity
import com.generals.module.home.ui.adapter.VP2Adapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private lateinit var homeActivity: HomeActivity
    private lateinit var mTabHome: TabLayout
    private lateinit var mVP2Home: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeActivity = activity as HomeActivity
        mTabHome = view.findViewById(R.id.tab_home)
        mVP2Home = view.findViewById(R.id.vp_home)

        initEvent()

    }

    private fun initEvent() {
        mTabHome.addTab(mTabHome.newTab().setText("发现"))
        mTabHome.addTab(mTabHome.newTab().setText("日报"))
        mTabHome.addTab(mTabHome.newTab().setText("推荐"))
        val fragmentList = listOf(
            { DailyFragment() },
            { RecommendFragment() }
        )
        mVP2Home.adapter = VP2Adapter(homeActivity, fragmentList)
        TabLayoutMediator(mTabHome,mVP2Home) { tab, position ->
            if(position == 0) {
                tab.text = "日报"
            }
            if(position == 1) {
                tab.text = "推荐"
            }
        }.attach()
    }

}