package com.generals.module_rankings.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2

class CustomConstraintLayout(context: Context, attributeSet: AttributeSet?) : ConstraintLayout(context, attributeSet){
    private var initialX = 0F
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