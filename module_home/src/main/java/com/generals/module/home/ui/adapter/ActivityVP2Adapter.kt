package com.generals.module.home.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @Desc : Activity+Fragment
 * @Author : zzx
 * @Date : 2025/7/23 19:18
 */

class ActivityVP2Adapter(
    fragmentActivity: FragmentActivity,
    private val list: List<() -> Fragment>
) : FragmentStateAdapter(fragmentActivity) {

    /**
     * 这里和fragment里面的vp2不同，这里的父级是activity
     */
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]()
    }
}