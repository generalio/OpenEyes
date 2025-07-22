package com.generals.module.square.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.generals.lib.base.Util
import com.generals.module.square.R
import com.generals.module.square.model.bean.Photo
import com.generals.module.square.ui.activity.SquareDetailActivity

/**
 * @Desc : 详情的Adapter
 * @Author : zzx
 * @Date : 2025/7/20 20:47
 */

class SquareDetailAdapter(private val photoList: List<Photo>) : RecyclerView.Adapter<SquareDetailAdapter.ViewHolder>(), PhotoAdapter.GetDetail {

    private var photoHeight = 0F

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photoViewPager2: ViewPager2 = view.findViewById(R.id.vp2_square_photo)
        val mTvCount: TextView = view.findViewById(R.id.tv_count_photo)
        val mTvDetail: TextView = view.findViewById(R.id.tv_detail)
        val layoutPhoto: View = view.findViewById(R.id.layout_photo)
        val layoutDetail: View = view.findViewById(R.id.layout_detail)
        val mIvAvatar: ImageView = view.findViewById(R.id.iv_detail_avatar)
        val mTvAuthorName: TextView = view.findViewById(R.id.tv_detail_author_name)
        val mTvCreateDate: TextView = view.findViewById(R.id.tv_detail_create_date)
        val mTvUpdateDate: TextView = view.findViewById(R.id.tv_detail_update_date)
        val mTvZan: TextView = view.findViewById(R.id.tv_detail_zan)
        val mTvDescription: TextView = view.findViewById(R.id.tv_detail_description)
        val mTvExit: TextView = view.findViewById(R.id.tv_detail_exit)

        // 注册vp2的监听接口，便于1/n数据的更新
        private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 用最新的 currentPhotoSize
                mTvCount.text = "${position + 1}/${photoList[bindingAdapterPosition].urls.size}"
                val height = layoutDetail.height.toFloat()
                val detailY = layoutDetail.bottom.toFloat()
                if((photoViewPager2.bottom - height) < detailY && layoutDetail.translationY == 0F) {
                    photoViewPager2.pivotY = 0f
                    val scale = (photoViewPager2.bottom - height) / detailY
                    photoViewPager2.scaleY = scale
                    photoViewPager2.scaleX = scale
                }
            }
        }

        init {
            // 将监听接口加入到vp中，这里不写在onBind中是防止多次重复创建
            photoViewPager2.registerOnPageChangeCallback(pageChangeCallback)
            // 将详情页从底部弹出
            mTvDetail.setOnClickListener {
                val height = layoutDetail.height.toFloat()
                val detailY = layoutDetail.bottom.toFloat()
                if(layoutDetail.translationY == 0F) {
                    layoutDetail.translationY = height
                }
                layoutDetail.animate().translationY(0F)
                if((photoViewPager2.bottom - height) < detailY) {
                    photoViewPager2.pivotY = 0f
                    val scale = (photoViewPager2.bottom - height) / detailY
                    photoViewPager2.animate().scaleY(scale)
                    photoViewPager2.animate().scaleX(scale)
                }
            }
            // 详情页弹回去
            itemView.setOnClickListener {
                if(layoutDetail.translationY == 0F) {
                    layoutDetail.animate().translationY(layoutDetail.height.toFloat())
                }
                if(photoViewPager2.scaleX != 1F || photoViewPager2.scaleY != 1F) {
                    photoViewPager2.animate().scaleX(1F)
                    photoViewPager2.animate().scaleY(1F)
                }
            }
            mTvExit.setOnClickListener {
                if(layoutDetail.translationY == 0F) {
                    layoutDetail.animate().translationY(layoutDetail.height.toFloat())
                }
                if(photoViewPager2.scaleX != 1F || photoViewPager2.scaleY != 1F) {
                    photoViewPager2.animate().scaleX(1F)
                    photoViewPager2.animate().scaleY(1F)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_square_detail, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.layoutDetail.post {
            holder.layoutDetail.translationY = holder.layoutDetail.height.toFloat()
        }
        holder.photoViewPager2.adapter = PhotoAdapter(photoList[position].urls, this)
        holder.mTvCount.text = "${holder.photoViewPager2.currentItem + 1} / ${photoList[holder.photoViewPager2.currentItem].urls.size}"
        Glide.with(holder.mIvAvatar.context)
            .load(photoList[position].avatar)
            .circleCrop()
            .into(holder.mIvAvatar)
        holder.mTvAuthorName.text = photoList[position].nickname
        holder.mTvCreateDate.text = "发布于 " + Util.transformDate(photoList[position].createTime)
        holder.mTvUpdateDate.text = "更新于 " + Util.transformDate(photoList[position].updateTime)
        holder.mTvZan.text = photoList[position].collectionCount.toString()
        holder.mTvDescription.text = photoList[position].description
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun getHeight(fl: Float) {
        photoHeight = fl
    }

}