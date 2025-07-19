package com.generals.module.square.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.generals.module.square.R
import com.generals.module.square.model.bean.Square

/**
 * @Desc : 广场的Adapter
 * @Author : zzx
 * @Date : 2025/7/18 20:47
 */

class SquareAdapter : ListAdapter<Square, SquareAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Square>() {
    override fun areContentsTheSame(oldItem: Square, newItem: Square): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Square, newItem: Square): Boolean {
        return oldItem.id == newItem.id
    }

}) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mIvCover: ImageView = view.findViewById(R.id.iv_square_cover)
        val mIvPicList: ImageView = view.findViewById(R.id.iv_square_pic_list)
        val mTvTitle: TextView = view.findViewById(R.id.tv_square_title)
        val mTvAuthorName: TextView = view.findViewById(R.id.tv_square_author_name)
        val mIvAvatar: ImageView = view.findViewById(R.id.iv_square_avatar)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val square = getItem(position)
        Glide.with(holder.mIvCover.context)
            .load(square.data.content.data.url.replace("http", "https"))
            .error(R.drawable.ic_failed)
            .apply(RequestOptions().centerCrop())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.mIvCover)
        holder.mIvPicList.isVisible = (square.data.content.data.urls.size > 1)
        holder.mTvTitle.text = square.data.content.data.description
        if(square.data.content.data.owner != null) {
            holder.mTvAuthorName.text = square.data.content.data.owner.nickname
            Glide.with(holder.mIvAvatar)
                .load(square.data.content.data.owner.avatar)
                .error(R.drawable.ic_avatar)
                .circleCrop()
                .into(holder.mIvAvatar)
        } else {
            holder.mTvAuthorName.text = "匿名用户"
            Glide.with(holder.mIvAvatar)
                .load(R.drawable.ic_avatar)
                .circleCrop()
                .into(holder.mIvAvatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_square, parent, false)
        return ViewHolder(view)
    }

}