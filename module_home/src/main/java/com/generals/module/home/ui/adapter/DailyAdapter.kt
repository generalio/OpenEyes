package com.generals.module.home.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.generals.lib.base.Util
import com.generals.module.home.R
import com.generals.module.home.model.bean.Daily

/**
 * @Desc : 日报RV的Adapter
 * @Author : zzx
 * @Date : 2025/7/14 11:18
 */

class DailyAdapter() : PagingDataAdapter<Daily, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<Daily>() {
    override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem.data.content.id == newItem.data.content.id
    }

} ) {

    private val TYPE_TITLE = 0
    private val TYPE_VIDEO = 1

    interface OnItemClickListener {
        fun onVideoClick(daily: Daily)
    }

    inner class TitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mTvTitle: TextView = view.findViewById(R.id.tv_title_title)
    }

    inner class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mIVCover: ImageView = view.findViewById(R.id.iv_video_cover)
        val mTvVideoTime: TextView = view.findViewById(R.id.tv_video_time)
        val mIvAvatar: ImageView = view.findViewById(R.id.iv_video_avatar)
        val mTvVideoTitle: TextView = view.findViewById(R.id.tv_video_title)
        val mTvDesc: TextView = view.findViewById(R.id.tv_video_desc)

        init {
            mIVCover.setOnClickListener {

            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val daily = getItem(position)
        daily?.let {
            when(holder) {
                is TitleViewHolder -> {
                    holder.mTvTitle.text = daily.data.text
                }
                is VideoViewHolder -> {
                    holder.mTvVideoTitle.text = daily.data.content.data.title
                    holder.mTvDesc.text = daily.data.content.data.author.name + " / #" + daily.data.content.data.category
                    Glide.with(holder.mIvAvatar.context)
                        .load(daily.data.content.data.author.icon)
                        .circleCrop()
                        .into(holder.mIvAvatar)
                    Glide.with(holder.mIVCover.context)
                        .load(daily.data.content.data.cover.feed)
                        .transform(RoundedCorners(40))
                        .into(holder.mIVCover)
                    holder.mTvVideoTime.text = Util.transferTime(daily.data.content.data.duration)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == TYPE_TITLE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_title,parent,false)
            return TitleViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video,parent,false)
            return VideoViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)!!
        if(item.type == "textCard") {
            return TYPE_TITLE
        } else {
            return TYPE_VIDEO
        }
    }

}