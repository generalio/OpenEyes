package com.generals.module.home.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @Desc : ViewPager2+Fragment的Adapter
 * @Author : zzx
 * @Date : 2025/7/13 16:23
 */

class VP2Adapter(fragmentActivity: Fragment, private val list: List<() -> Fragment>) : FragmentStateAdapter(fragmentActivity) {

    /**
     * 这里的父级owner需要为fragment而不能为activity，不然FragmentStateAdapter会把activity的FragmentManager存进去
     * 如果fragment被销毁了，但是activity仍然存在时，vp2仍然持有这个适配器，适配器又持有这个activity
     * 就会导致无法被回收从而内存泄漏
     */
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]()
    }
}