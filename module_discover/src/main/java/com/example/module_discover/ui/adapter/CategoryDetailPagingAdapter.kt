package com.example.module_discover.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.module_discover.R
import com.example.module_discover.model.bean.CategoryDetailItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CategoryDisplayAdapter(
    private val onItemClick: (CategoryDetailItem) -> Unit = {},
    private val onShareClick: (CategoryDetailItem) -> Unit = {}
) : PagingDataAdapter<CategoryDetailItem, CategoryDisplayAdapter.CategoryDisplayViewHolder>(DIFF_CALLBACK) {

    companion object {
        private const val TAG = "CategoryDisplayAdapter"

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CategoryDetailItem>() {
            override fun areItemsTheSame(oldItem: CategoryDetailItem, newItem: CategoryDetailItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CategoryDetailItem, newItem: CategoryDetailItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryDisplayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_catagory_video, parent, false)
        return CategoryDisplayViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryDisplayViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, position)
        } else {
            holder.hideItem()
        }
    }

    inner class CategoryDisplayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val authorIcon: ImageView = itemView.findViewById(R.id.imageView)
        private val authorName: TextView = itemView.findViewById(R.id.textView)
        private val videoInfo: TextView = itemView.findViewById(R.id.textView2)
        private val videoThumbnail: ImageView = itemView.findViewById(R.id.imageView3)
        private val description: TextView = itemView.findViewById(R.id.textView3)
        private val likeCount: TextView = itemView.findViewById(R.id.textView4)
        private val collectCount: TextView = itemView.findViewById(R.id.textView5)
        private val commentCount: TextView = itemView.findViewById(R.id.textView6)
        private val shareButton: Button = itemView.findViewById(R.id.button2)

        fun bind(item: CategoryDetailItem, position: Int) {
            try {
                itemView.visibility = View.VISIBLE
                val layoutParams = itemView.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                itemView.layoutParams = layoutParams

                // 根据实际JSON数据结构获取信息
                when (item.type) {
                    "followCard" -> {
                        bindFollowCard(item, position)
                    }
                    else -> {
                        // 其他类型暂时隐藏
                        hideItem()
                        return
                    }
                }

                // 设置点击事件
                itemView.setOnClickListener { onItemClick(item) }
                shareButton.setOnClickListener { onShareClick(item) }

                Log.d(TAG, "成功绑定数据 - position: $position, 类型: ${item.type}")

            } catch (e: Exception) {
                Log.e(TAG, "绑定数据时出现异常 - position: $position", e)
                hideItem()
            }
        }

        private fun bindFollowCard(item: CategoryDetailItem, position: Int) {
            try {
                // 根据你的JSON数据结构，正确获取数据
                val data = item.data
                val header = data?.header
                val content = data?.content
                val videoData = content?.data

                // 设置作者信息 - 优先使用header中的信息
                val authorNameText = header?.title ?: videoData?.author?.name ?: "未知作者"
                authorName.text = authorNameText

                // 设置发布时间 - 使用header中的time
                val timestamp = header?.time ?: videoData?.releaseTime
                videoInfo.text = formatTime(timestamp)

                // 设置描述 - 优先使用视频的描述
                val desc = videoData?.descriptionEditor?.takeIf { it.isNotEmpty() }
                    ?: videoData?.description?.takeIf { it.isNotEmpty() }
                    ?: videoData?.title?.takeIf { it.isNotEmpty() }
                    ?: "无描述"
                description.text = desc

                // 设置互动数据
                val consumption = videoData?.consumption
                likeCount.text = formatCount(consumption?.collectionCount ?: 0)
                collectCount.text = formatCount(consumption?.realCollectionCount ?: 0)
                commentCount.text = formatCount(consumption?.replyCount ?: 0)

                // 加载作者头像 - 优先使用header中的icon
                loadAuthorIcon(header?.icon ?: videoData?.author?.icon)

                // 加载视频缩略图
                loadVideoThumbnail(videoData?.cover?.feed)

                Log.d(TAG, "bindFollowCard成功 - 标题: ${videoData?.title}")

            } catch (e: Exception) {
                Log.e(TAG, "bindFollowCard失败", e)
                hideItem()
            }
        }

        private fun loadAuthorIcon(iconUrl: String?) {
            if (!iconUrl.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(iconUrl)
                    .circleCrop()
                    .placeholder(R.drawable.test_icon)
                    .error(R.drawable.test_icon)
                    .into(authorIcon)
            } else {
                // 没有图片时显示默认头像
                Glide.with(itemView.context)
                    .load(R.drawable.test_icon)
                    .circleCrop()
                    .into(authorIcon)
            }
        }

        private fun loadVideoThumbnail(thumbnailUrl: String?) {
            if (!thumbnailUrl.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(thumbnailUrl)
                    .placeholder(R.drawable.test_img)
                    .error(R.drawable.test_img)
                    .into(videoThumbnail)
            } else {
                // 没有图片时显示默认图片
                videoThumbnail.setImageResource(R.drawable.test_img)
            }
        }

        fun hideItem() {
            itemView.visibility = View.GONE
            val layoutParams = itemView.layoutParams
            layoutParams.height = 0
            itemView.layoutParams = layoutParams
        }
    }

    private fun formatCount(count: Int): String {
        return when {
            count < 1000 -> count.toString()
            count < 10000 -> String.format("%.1fk", count / 1000.0)
            else -> String.format("%.1fw", count / 10000.0)
        }
    }

    private fun formatTime(timestamp: Long?): String {
        return try {
            if (timestamp == null || timestamp <= 0) {
                "未知时间"
            } else {
                val sdf = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())
                sdf.format(Date(timestamp))
            }
        } catch (e: Exception) {
            "未知时间"
        }
    }
}