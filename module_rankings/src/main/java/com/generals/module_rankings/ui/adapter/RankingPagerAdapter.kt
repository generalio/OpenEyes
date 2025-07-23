package com.generals.module_rankings.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.generals.module_rankings.ui.fragment.MonthRankingFragment
import com.generals.module_rankings.ui.fragment.TotalRankingFragment
import com.generals.module_rankings.ui.fragment.VideoFragment

class RankingPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2 // 两个选项卡

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> VideoFragment() // 月排行 Fragment
            1 -> TotalRankingFragment() // 总排行 Fragment
            else -> MonthRankingFragment()
        }
    }
}