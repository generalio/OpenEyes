package com.generals.lib.base

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat

/**
 * @Desc : Activity基类
 * @Author : zzx
 * @Date : 2025/7/13 10:19
 */

abstract class BaseActivity : AppCompatActivity() {

    /**
     * 是否沉浸式状态栏
     *
     * ## 注意
     * 沉浸式后，状态栏不会再有东西占位，界面会默认上移，
     * 可以给布局加上 android:fitsSystemWindows=true (但不建议给根布局加，一般是给第二个布局加)，
     * 不同布局该属性效果不同，请给合适的布局添加
     *
     * ## 比如
     * - 大部分情况下是给第二层布局添加 fitsSystemWindows=true，因为最外层布局需要提供背景给状态栏，而第二层布局需要下移状态栏
     * - 如果你使用了 BottomSheet，那么大概率需要给 BottomSheet 加上 fitsSystemWindows=true。
     *   (注意: CoordinatorLayout 设置 fitsSystemWindows 无效，但可以在外面包一层 FrameLayout，给它加上 fitsSystemWindows，具体可以看 main 模块里面的课表写法)
     * -
     */
    protected open val isCancelStatusBar: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(isCancelStatusBar)cancelStatusBar()
    }

    private fun cancelStatusBar() {
        val window = this.window
        val decorView = window.decorView

        // 这是 Android 做了兼容的 Compat 包
        // 注意，使用了下面这个方法后，状态栏不会再有东西占位，
        // 可以给根布局加上 android:fitsSystemWindows=true
        // 不同布局该属性效果不同，请给合适的布局添加
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController = WindowCompat.getInsetsController(window, decorView)
        // 如果你要白色的状态栏字体，请在你直接的 Activity 中单独设置 isAppearanceLightStatusBars，这里不提供方法
        //windowInsetsController.isAppearanceLightStatusBars = isDaytimeMode()
        window.statusBarColor = Color.TRANSPARENT //把状态栏颜色设置成透明
    }

    //检查网络是否链接
    @SuppressLint("ServiceCast")
    fun isNetworkAvailable(): Boolean {
        // 这里不能用context.getSystemService()，容易导致内存泄漏，应该使用applicationContext,这样持有的就是application了
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        val isAvailable = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        return isAvailable
    }

}