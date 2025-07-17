package com.generals.module.home.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

/**
 * @Desc : 解决VP2嵌套滑动
 * @Author : zzx
 * @Date : 2025/7/17 16:30
 */

class CustomConstraintLayout(context: Context, attributeSet: AttributeSet?) : ConstraintLayout(context, attributeSet) {

    private var initialX = 0F

    // 仍会有问题，在快速滑动(fling)时，canScrollHorizontally方法可能会失效，无法禁止父布局拦截
    // 即可能方法得出不能再滑但是实际上是可以滑的，此时就会返回false，不禁止父布局拦截
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = ev.x
                parent.requestDisallowInterceptTouchEvent(true) // 禁止父布局拦截
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = ev.x - initialX
                val child = getChildAt(1) as ViewPager2
                val canScroll = if (dx > 0) {
                    child.canScrollHorizontally(-1) // 右滑，是否还能再向左滑,如果还能左滑那么就为true,不允许父布局再滑
                } else {
                    child.canScrollHorizontally(1)  // 左滑，是否还能再向右滑， 同理
                }
                // 只有不能再滑时，才不禁止父布局进行拦截
                parent.requestDisallowInterceptTouchEvent(canScroll)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

}