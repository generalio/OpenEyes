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
import com.generals.module.home.model.bean.daily.Daily
import com.generals.module.home.model.bean.recommend.Recommend

/**
 * @Desc : 推荐页的Adapter
 * @Author : zzx
 * @Date : 2025/7/14 20:35
 */

class RecommendAdapter(val itemClickListener: OnItemClickListener) : PagingDataAdapter<Recommend, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<Recommend>() {
    override fun areContentsTheSame(oldItem: Recommend, newItem: Recommend): Boolean {
        return newItem == oldItem
    }

    override fun areItemsTheSame(oldItem: Recommend, newItem: Recommend): Boolean {
//        var newId = 0
//        if(newItem.data.type == "textCard") {
//            newId = newItem.data.id
//        } else {
//            if(newItem.data.type == "followCard") {
//                newId = newItem.data.content.data.id
//            } else {
//                if(newItem.data.type == "videoSmallCard") {
//                    newId = newItem.data.id
//                }
//            }
//        }
//
//        var oldId = 0
//        if(oldItem.data.type == "textCard") {
//            oldId = oldItem.data.id
//        } else {
//            if(oldItem.data.type == "followCard") {
//                oldId = oldItem.data.content.data.id
//            } else {
//                if(oldItem.data.type == "videoSmallCard") {
//                    oldId = oldItem.data.id
//                }
//            }
//        }
        return newItem == oldItem
    }

}) {

    companion object {
        private val TYPE_TITLE = 0
        private val TYPE_VIDEO = 1
        private val TYPE_VIDEO_SMALL = 2
    }

    interface OnItemClickListener {
        fun onVideoClick(recommend: Recommend)
        fun onVideoSmallClick(recommend: Recommend)
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

    inner class VideoSmallViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mIvCover: ImageView = view.findViewById(R.id.iv_video_small_cover)
        val mTvVideoTime: TextView = view.findViewById(R.id.tv_video_small_time)
        val mTvVideoTitle: TextView = view.findViewById(R.id.tv_video_small_title)
        val mTvDesc: TextView = view.findViewById(R.id.tv_video_small_desc)

        init {
            mIvCover.setOnClickListener {
                itemClickListener.onVideoSmallClick(getItem(bindingAdapterPosition)!!)
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
                is VideoSmallViewHolder -> {
                    holder.mTvVideoTitle.text = daily.data.title
                    holder.mTvDesc.text = daily.data.author.name + " / #" + daily.data.category
                    Glide.with(holder.mIvCover.context)
                        .load(daily.data.cover.feed)
                        .transform(RoundedCorners(40))
                        .into(holder.mIvCover)
                    holder.mTvVideoTime.text = Util.transferTime(daily.data.duration)
                }

                else -> {}
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == TYPE_TITLE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_title, parent, false)
            return TitleViewHolder(view)
        } else {
            if(viewType == TYPE_VIDEO) {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
                return VideoViewHolder(view)
            } else {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video_small, parent, false)
                return VideoSmallViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)!!
        if(item.type == "textCard") {
            return TYPE_TITLE
        } else {
            if(item.type == "followCard") {
                return TYPE_VIDEO
            } else {
                return TYPE_VIDEO_SMALL
            }
        }
    }
}