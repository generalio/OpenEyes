package com.generals.module.video.ui.adapter

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
import com.generals.module.video.R
import com.generals.module.video.model.bean.Comment
import com.generals.module.video.model.bean.UserX
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * @Desc : 类的描述
 * @Author : zzx
 * @Date : 2025/7/16 17:17
 */

class NewCommentAdapter :
    PagingDataAdapter<Comment, NewCommentAdapter.NewCommentViewHolder>(object :
        DiffUtil.ItemCallback<Comment>() {
        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return newItem == oldItem
        }

        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.data.id == newItem.data.id
        }

    }) {

    inner class NewCommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mIvAvatar: ImageView = view.findViewById(R.id.iv_comment_avatar)
        val mTvUsername: TextView = view.findViewById(R.id.tv_comment_username)
        val mTvDate: TextView = view.findViewById(R.id.tv_comment_date)
        val mTvContent: TextView = view.findViewById(R.id.tv_comment_content)
        val mTvZan: TextView = view.findViewById(R.id.tv_comment_zan)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NewCommentViewHolder, position: Int) {
        val comment = getItem(position)!!
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        holder.mTvDate.text = sdf.format(Date(comment.data.createTime))
        holder.mTvContent.text = comment.data.message
        holder.mTvZan.text = comment.data.likeCount.toString()
        val user = comment.data.user
        if (user != null) {
            holder.mTvUsername.text = user.nickname
            Glide.with(holder.mIvAvatar.context)
                .load(user.avatar)
                .error(R.drawable.ic_avatar)
                .circleCrop()
                .into(holder.mIvAvatar)
        } else {
            holder.mTvUsername.text = "匿名用户"
            Glide.with(holder.mIvAvatar.context)
                .load(R.drawable.ic_avatar)
                .circleCrop()
                .into(holder.mIvAvatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewCommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return NewCommentViewHolder(view)
    }
}