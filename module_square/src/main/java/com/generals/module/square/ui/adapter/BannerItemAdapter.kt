package com.generals.module.square.ui.adapter

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.generals.module.square.R
import com.generals.module.square.model.bean.ItemDetail
import com.generals.module.square.ui.custom.BannerTransformer

/**
 * @Desc : 轮播图item的Adapter
 * @Author : zzx
 * @Date : 2025/7/18 14:34
 */

class BannerItemAdapter(private val bannerList: List<ItemDetail>) : RecyclerView.Adapter<BannerItemAdapter.ViewHolder>() {

    lateinit var handler: Handler
    lateinit var runnable: Runnable

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mVP2Banner: ViewPager2 = view.findViewById(R.id.vp2_banner)

        init {
            // 自动播放
            handler = Handler(Looper.getMainLooper())
            runnable = object : Runnable {
                override fun run() {
                    val nextPage = mVP2Banner.currentItem + 1
                    if(nextPage == bannerList.size) {
                        mVP2Banner.setCurrentItem(1, false)
                    } else {
                        mVP2Banner.currentItem = nextPage
                    }
                    handler.postDelayed(this, 3000)
                }
            }
            handler.postDelayed(runnable, 3000) //启动自动播放

            // 手动切换
            mVP2Banner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    if(state == ViewPager2.SCROLL_STATE_DRAGGING) {
                        handler.removeCallbacks(runnable)
                    } else {
                        if(state == ViewPager2.SCROLL_STATE_IDLE) {
                            if(mVP2Banner.currentItem == bannerList.size - 1) {
                                mVP2Banner.setCurrentItem(1, false)
                            }
                            if(mVP2Banner.currentItem == 0) {
                                mVP2Banner.setCurrentItem(bannerList.size - 2, false)
                            }
                            handler.removeCallbacks(runnable)
                            handler.postDelayed(runnable, 3000)
                        }
                    }
                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
            val rv = mVP2Banner.getChildAt(0) as RecyclerView
            rv.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                var initialX = 0F
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    when(e.action) {
                        MotionEvent.ACTION_DOWN -> {
                            initialX = e.x
                            mVP2Banner.parent.requestDisallowInterceptTouchEvent(true)
                        }
                        MotionEvent.ACTION_MOVE -> {
                            val dx = e.x - initialX
                            if(dx > 0 && !rv.canScrollHorizontally(-1)) {
                                mVP2Banner.parent.requestDisallowInterceptTouchEvent(false)
                            } else if(dx < 0 && !rv.canScrollHorizontally(1)) {
                                mVP2Banner.parent.requestDisallowInterceptTouchEvent(false)
                            }
                        }
                    }
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_banner, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val adapter = BannerContentAdapter(bannerList)
        holder.mVP2Banner.adapter = adapter
        holder.mVP2Banner.setPageTransformer(BannerTransformer())
        holder.mVP2Banner.setCurrentItem(bannerList.size / 2, false)
        holder.mVP2Banner.offscreenPageLimit = 3

    }

    override fun getItemCount(): Int {
        return 1
    }

    fun release() {
        handler.removeCallbacksAndMessages(null)
    }

}