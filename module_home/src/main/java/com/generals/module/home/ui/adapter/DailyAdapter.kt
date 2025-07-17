package com.generals.module.home.ui.adapter

import android.annotation.SuppressLint
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
import com.generals.module.home.model.bean.daily.Daily

/**
 * @Desc : 日报RV的Adapter
 * @Author : zzx
 * @Date : 2025/7/14 11:18
 */

class DailyAdapter(val itemClickListener: OnItemClickListener) : PagingDataAdapter<Daily, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<Daily>() {
    /**
     * 接口没有唯一ID标识符(id基本都是0，header字段里的id不为0，但是text又没有header字段容易爆红)
     * 故这里只能粗暴处理，导致每次切换后台回来都会重新刷新一遍
     * 二则：解决了不是差分刷新的问题，是flow的问题，详细见viewmodel
     */
    override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return newItem == oldItem
    }

    override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
//        var newId = 0
//        if(newItem.data.type == "textCard") {
//            newId = oldItem.data.id
//        } else {
//            if(newItem.data.type == "followCard") {
//                newId = newItem.data.content.data.id
//            }
//        }
//
//        var oldId = 0
//        if(oldItem.data.type == "textCard") {
//            oldId = oldItem.data.id
//        } else {
//            if(oldItem.data.type == "followCard") {
//                oldId = oldItem.data.content.data.id
//            }
//        }
        return newItem == oldItem // 粗暴解决
    }

} ) {

    companion object {
        private val TYPE_TITLE = 0
        private val TYPE_VIDEO = 1
    }

    interface OnItemClickListener {
        fun onVideoClick(daily: Daily)
    }

    inner class TitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mTvTitle: TextView = view.findViewById(R.id.tv_title_title)
    }

    inner class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mIvCover: ImageView = view.findViewById(R.id.iv_video_cover)
        val mTvVideoTime: TextView = view.findViewById(R.id.tv_video_time)
        val mIvAvatar: ImageView = view.findViewById(R.id.iv_video_avatar)
        val mTvVideoTitle: TextView = view.findViewById(R.id.tv_video_title)
        val mTvDesc: TextView = view.findViewById(R.id.tv_video_desc)

        init {
            mIvCover.setOnClickListener {
                itemClickListener.onVideoClick(getItem(bindingAdapterPosition)!!)
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
                    Glide.with(holder.mIvCover.context)
                        .load(daily.data.content.data.cover.feed)
                        .transform(RoundedCorners(40))
                        .into(holder.mIvCover)
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