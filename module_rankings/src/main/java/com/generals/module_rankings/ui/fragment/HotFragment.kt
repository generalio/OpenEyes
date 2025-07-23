package com.generals.module_rankings.ui.fragment

import RankingViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.module_rankings.R
import com.generals.module_rankings.ui.adapter.RankingPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HotFragment:Fragment() {
    private lateinit var viewModel: RankingViewModel
    private lateinit var mTabHome: TabLayout
    private lateinit var mVP2Home: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[RankingViewModel::class.java]

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_main, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTabHome = view.findViewById(R.id.tab_home)
        mVP2Home = view.findViewById(R.id.vp_home)

        mVP2Home.isUserInputEnabled = true

        initEvent()

        viewModel.getMonthlyRanking()
        viewModel.getTotalRanking()
    }

    private fun initEvent() {
        val adapter = RankingPagerAdapter(this)
        mVP2Home.adapter = adapter

        // 设置页面变换器
        mVP2Home.setPageTransformer(createPageTransformer())

        TabLayoutMediator(mTabHome, mVP2Home) { tab, position ->
            Log.d("HotFragment", "Setting up tab at position: $position")
            when (position) {
                0 -> tab.text = "月排行"
                1 -> tab.text = "总排行"
            }
        }.attach()

        mVP2Home.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                Log.d("HotFragment", "Page selected: $position")
            }
        })
    }

    // 创建页面变换器
    private fun createPageTransformer(): ViewPager2.PageTransformer {
        return ViewPager2.PageTransformer { page, position ->
            when {
                position < -1 -> {
                    // 完全不可见的页面
                    page.alpha = 0f
                }
                position <= 1 -> {
                    // 可见或部分可见的页面
                    page.alpha = 1f

                    // 淡入淡出效果
                    page.alpha = 1 - kotlin.math.abs(position)

                    // 缩放效果
                    val scaleFactor = kotlin.math.max(0.85f, 1 - kotlin.math.abs(position) * 0.15f)
                    page.scaleX = scaleFactor
                    page.scaleY = scaleFactor

                    // 平移效果
                    page.translationX = -position * page.width * 0.25f
                }
                else -> {
                    // 完全不可见的页面
                    page.alpha = 0f
                }
            }
        }
    }
}