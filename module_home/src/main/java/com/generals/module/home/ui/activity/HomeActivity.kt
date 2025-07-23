package com.generals.module.home.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.module_discover.ui.fragment.DiscoverFragment
import com.generals.lib.base.BaseActivity
import com.generals.module.home.R
import com.generals.module.home.ui.adapter.ActivityVP2Adapter
import com.generals.module.home.ui.adapter.VP2Adapter
import com.generals.module.home.ui.fragment.HomeFragment
import com.generals.module.square.ui.fragment.SquareFragment
import com.generals.module_rankings.ui.fragment.HotFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

@Route(path = "/home/HomeActivity")
class HomeActivity : BaseActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        ARouter.getInstance().inject(this)
        initView()
        initEvent()

    }

    private fun initView() {
        viewPager2 = findViewById(R.id.vp_main)
        bottomNavigation = findViewById(R.id.navigation_home)
    }

    private fun initEvent() {
        val fragmentList = listOf(
            { HomeFragment() },
            { SquareFragment() },
            { DiscoverFragment() },
            { HotFragment() }
        )
        viewPager2.adapter = ActivityVP2Adapter(this, fragmentList)
//        viewPager2.isUserInputEnabled = false
        bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.bottom_home -> {
                    viewPager2.setCurrentItem(0, false)
                    return@setOnItemSelectedListener true
                }
                R.id.bottom_square -> {
                    viewPager2.setCurrentItem(1, false)
                    return@setOnItemSelectedListener true
                }
                R.id.bottom_found -> {
                    viewPager2.setCurrentItem(2, false)
                    return@setOnItemSelectedListener true
                }
                R.id.bottom_hot -> {
                    viewPager2.setCurrentItem(3,false)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottomNavigation.menu.getItem(position).isChecked = true
            }
        })
    }

    fun showToast(content: String) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
    }
}