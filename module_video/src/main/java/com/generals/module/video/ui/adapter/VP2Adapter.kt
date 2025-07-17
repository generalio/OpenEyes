package com.generals.module.video.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @Desc : VP2+Fragment的Adapter
 * @Author : zzx
 * @Date : 2025/7/15 20:41
 */

class VP2Adapter(fragmentActivity: FragmentActivity, private val list: List<() -> Fragment>) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]()
    }
}