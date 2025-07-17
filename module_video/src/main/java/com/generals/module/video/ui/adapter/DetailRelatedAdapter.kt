package com.generals.module.video.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.generals.lib.base.Util
import com.generals.module.video.R
import com.generals.module.video.model.bean.related.VideoRelated

/**
 * @Desc : 推荐视频列表的Adapter
 * @Author : zzx
 * @Date : 2025/7/17 15:05
 */

class DetailRelatedAdapter(private val itemClickListener: OnItemClickListener) : ListAdapter<VideoRelated, DetailRelatedAdapter.ViewHolder>(object : DiffUtil.ItemCallback<VideoRelated>() {
    override fun areContentsTheSame(oldItem: VideoRelated, newItem: VideoRelated): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: VideoRelated, newItem: VideoRelated): Boolean {
        return newItem.data.id == oldItem.data.id
    }

}) {

    interface OnItemClickListener {
        fun onVideoCoverClick(videoInfo: VideoRelated)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mIvAvatar: ImageView = view.findViewById(R.id.iv_related_avatar)
        val mTvTitle: TextView = view.findViewById(R.id.tv_related_title)
        val mTvAuthorName: TextView = view.findViewById(R.id.tv_related_author_name)
        val mIvCover: ImageView = view.findViewById(R.id.iv_related_cover)
        val mTvTime: TextView = view.findViewById(R.id.tv_related_time)

        init {
            mIvCover.setOnClickListener {
                itemClickListener.onVideoCoverClick(getItem(bindingAdapterPosition))
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val videoInfo = getItem(position)
        videoInfo?.let {
            holder.itemView.alpha = 0F
            holder.itemView.animate().alpha(1F).setDuration(600).start()
            val author = videoInfo.data.author
            if(author != null) {
                Glide.with(holder.mIvAvatar.context)
                    .load(videoInfo.data.author.icon)
                    .circleCrop()
                    .into(holder.mIvAvatar)
            } else {
                Glide.with(holder.mIvAvatar.context)
                    .load(R.drawable.ic_avatar)
                    .circleCrop()
                    .into(holder.mIvAvatar)
            }
            if(author.name != null) {
                holder.mTvAuthorName.text = videoInfo.data.author.name
            } else {
                holder.mTvAuthorName.text = "匿名用户"
            }
            holder.mTvTitle.text = videoInfo.data.title
            Glide.with(holder.mIvCover.context)
                .load(videoInfo.data.cover.feed)
                .into(holder.mIvCover)
            holder.mTvTime.text = Util.transferTime(videoInfo.data.duration)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail_related, parent, false)
        return ViewHolder(view)
    }

}