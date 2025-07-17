package com.generals.module.video.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.generals.module.video.R
import com.generals.module.video.model.bean.VideoInfo

/**
 * @Desc : 简介详情的Adapter
 * @Author : zzx
 * @Date : 2025/7/17 11:03
 */

class DetailInfoAdapter(private val videoInfo: VideoInfo, private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<DetailInfoAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onZanClick()
        fun onCollectClick()
        fun onCommentClick()
        fun onShareClick()
        fun onDescriptionClick()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mTvTitle: TextView = view.findViewById(R.id.tv_detail_title)
        val mTvSubTitle: TextView = view.findViewById(R.id.tv_detail_subTitle)
        val mTvDescription: TextView = view.findViewById(R.id.tv_detail_description)
        val mTvZan: TextView = view.findViewById(R.id.tv_detail_zan)
        val mTvCollect: TextView = view.findViewById(R.id.tv_detail_collect)
        val mTvComment: TextView = view.findViewById(R.id.tv_detail_comment)
        val mTvShare: TextView = view.findViewById(R.id.tv_detail_share)
        val mIvAvatar: ImageView = view.findViewById(R.id.iv_detail_avatar)
        val mTvAuthorName: TextView = view.findViewById(R.id.tv_detail_author_name)
        val mTvAuthorDescription: TextView = view.findViewById(R.id.tv_detail_author_description)

        init {
            mTvZan.setOnClickListener {
                itemClickListener.onZanClick()
            }
            mTvCollect.setOnClickListener {
                itemClickListener.onCollectClick()
            }
            mTvComment.setOnClickListener {
                itemClickListener.onCommentClick()
            }
            mTvShare.setOnClickListener {
                itemClickListener.onShareClick()
            }
            mTvDescription.setOnLongClickListener {
                itemClickListener.onDescriptionClick()
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail_info, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.alpha = 0F
        holder.itemView.animate().alpha(1F).setDuration(600).start()
        holder.mTvTitle.text = videoInfo.title
        holder.mTvSubTitle.text = videoInfo.subTitle
        holder.mTvDescription.text = videoInfo.description
        holder.mTvZan.text = videoInfo.likeCount.toString()
        holder.mTvCollect.text = videoInfo.collectionCount.toString()
        holder.mTvShare.text = videoInfo.shareCount.toString()
        holder.mTvComment.text = videoInfo.replyCount.toString()
        holder.mTvAuthorName.text = videoInfo.authorName
        holder.mTvAuthorDescription.text = videoInfo.authorDescription
        Glide.with(holder.mIvAvatar.context)
            .load(videoInfo.authorIcon)
            .circleCrop()
            .into(holder.mIvAvatar)
    }

    override fun getItemCount(): Int {
        return 1
    }

}