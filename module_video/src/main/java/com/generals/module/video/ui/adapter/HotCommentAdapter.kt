package com.generals.module.video.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.generals.module.video.R
import com.generals.module.video.model.bean.Comment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * @Desc : 热门评论列表的Adapter
 * @Author : zzx
 * @Date : 2025/7/16 15:48
 */

class HotCommentAdapter : ListAdapter<Comment, HotCommentAdapter.HotCommentViewHolder>(object : DiffUtil.ItemCallback<Comment>() {
    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.data.id == newItem.data.id
    }

}) {

    inner class HotCommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mIvAvatar: ImageView = view.findViewById(R.id.iv_comment_avatar)
        val mTvUsername: TextView = view.findViewById(R.id.tv_comment_username)
        val mTvDate: TextView = view.findViewById(R.id.tv_comment_date)
        val mTvContent: TextView = view.findViewById(R.id.tv_comment_content)
        val mTvZan: TextView = view.findViewById(R.id.tv_comment_zan)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HotCommentViewHolder, position: Int) {
        val comment = getItem(position)
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        holder.mTvDate.text = sdf.format(Date(comment.data.createTime))
        holder.mTvContent.text = comment.data.message
        holder.mTvZan.text = comment.data.likeCount.toString()
        if(comment.data.user != null) {
            Glide.with(holder.mIvAvatar.context)
                .load(comment.data.user.avatar)
                .error(R.drawable.ic_avatar)
                .circleCrop()
                .into(holder.mIvAvatar)
            holder.mTvUsername.text = comment.data.user.nickname
        } else {
            holder.mTvUsername.text = "匿名用户"
            Glide.with(holder.mIvAvatar.context)
                .load(R.drawable.ic_avatar)
                .circleCrop()
                .into(holder.mIvAvatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotCommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return HotCommentViewHolder(view)
    }
}