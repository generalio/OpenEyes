package com.generals.module.video.ui.custom

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.NestedScrollingParent2
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat
import kotlin.math.max
import kotlin.math.min

/**
 * @Desc : 带加载界面的Layout
 * @Author : zzx
 * @Date : 2025/7/16 20:05
 */

class LoadingLayout(context: Context, attributeSet: AttributeSet?) : FrameLayout(context, attributeSet), NestedScrollingParent2 {

    private lateinit var loading: ImageView
    private lateinit var otherLayout: View

    private var loadingHeight = 0F
    private var maxSlopHeight = 0F
    private var rotateAnimator: ObjectAnimator? = null

    private val helper = NestedScrollingParentHelper(this)

    var onRotateStart: (() -> Unit)? = null // 暴露一个动画开始的接口
    var onRotateStop: (() -> Unit)? = null // 暴露一个动画结束的接口

    override fun onFinishInflate() {
        super.onFinishInflate()
        loading = getChildAt(0) as ImageView
        otherLayout = getChildAt(1)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if(maxSlopHeight == 0F) {
            maxSlopHeight = -loading.y * 3 // 最大滑动值为距离顶部的两倍
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        loadingHeight =loading.measuredHeight.toFloat()
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0 //垂直滑动处理
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        helper.onNestedScrollAccepted(child, target, axes, type)
    }

    @SuppressLint("Recycle")
    override fun onStopNestedScroll(target: View, type: Int) {
        // 回弹回初始位置
        if(loading.translationY < maxSlopHeight / 3 * 2) {
            loading.animate().translationY(0F).setInterpolator(DecelerateInterpolator())
            otherLayout.animate().translationY(0F).setInterpolator(DecelerateInterpolator())
        } else {
            startAnimate()
        }
    }

    fun startAnimate() {
        // 回弹到一定高度进行旋转
        loading.animate().translationY(maxSlopHeight / 3 * 2).setInterpolator(DecelerateInterpolator())
        otherLayout.animate().translationY(maxSlopHeight / 3 * 2).setInterpolator(DecelerateInterpolator())
        if (rotateAnimator?.isRunning == true) return //防止重复创建
        rotateAnimator = ObjectAnimator.ofFloat(loading, "rotation", loading.rotation, loading.rotation - 360F).apply {
            duration = 1500
            repeatCount = 0
            interpolator = LinearInterpolator()
            addListener(object : AnimatorListener {
                override fun onAnimationStart(p0: Animator) {
                    // 暴露一个接口开始刷新
                    onRotateStart?.invoke()
                }

                override fun onAnimationEnd(p0: Animator) {
                    // 暴露一个接口结束刷新
                    onRotateStop?.invoke()
                }

                override fun onAnimationCancel(p0: Animator) {}

                override fun onAnimationRepeat(p0: Animator) {}

            })
            start()
        }
    }

    fun stopAnimate() {
        loading.animate().translationY(0F).setInterpolator(DecelerateInterpolator())
        otherLayout.animate().translationY(0F).setInterpolator(DecelerateInterpolator())
        rotateAnimator?.cancel()
        rotateAnimator = null
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        //表示子View已达到顶部而还有未消费的向下滑的距离， > 0则相反
        //忽略fling/惯性滑动，防止奇怪的动画
        if(dyUnconsumed < 0 && type == ViewCompat.TYPE_TOUCH) {
            val consume = min(-dyUnconsumed.toFloat(), maxSlopHeight) // 未消费的值和最大滑动值取最小
            move(consume)
        }
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        var consume = 0
        if(dy > 0 && loading.translationY > 0) { // 如果向上滑时(dy > 0)且loading还未返回原位
            consume = min(dy, loading.translationY.toInt())
            move(-consume.toFloat())
        }
        consumed[1] = consume
    }

    // translationY / max * 360
    private fun move(transition: Float) {
        val transitionY = (loading.translationY + transition).coerceIn(0F,maxSlopHeight)
        loading.translationY = transitionY
        otherLayout.translationY = transitionY
        loading.rotation = -transitionY / maxSlopHeight * 360F
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        rotateAnimator?.cancel()
        rotateAnimator = null
    }
}