package com.generals.module.square.ui.custom

import android.view.View
import android.view.translation.TranslationSpec
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

/**
 * @Desc : 轮播图的Transformer
 * @Author : zzx
 * @Date : 2025/7/18 15:44
 */

class BannerTransformer : ViewPager2.PageTransformer {

    companion object {
        private val MIN_VALUE = 0.8F
        private val MAX_VALUE = 1.0F
        private val MIN_ALPHA = 0.5F
        private val MAX_ALPHA = 1.0F
        private val Translation = 150F
    }

    // -1 0 1
    override fun transformPage(page: View, position: Float) {
        // 中间大，两边小
        val transition = MIN_VALUE + (1 - abs(position))  * (MAX_VALUE - MIN_VALUE)
        val transitionAlpha = MIN_ALPHA + (1 - abs(position)) * (MAX_ALPHA - MIN_ALPHA)
        page.scaleX = transition
        page.scaleY = transition // 缩放
        page.alpha = transitionAlpha // 改变透明度
        page.translationX = -position * Translation // 中间与左右重叠
        page.z = abs(position) // 更改z轴
    }
}