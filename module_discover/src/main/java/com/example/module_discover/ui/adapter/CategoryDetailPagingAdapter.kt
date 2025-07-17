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
import java.util.*

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
        if (item != null && canDisplayItem(item)) {
            holder.bind(item, position)
        } else {
            // 不能显示的item，隐藏这个ViewHolder
            holder.hideItem()
        }
    }

    private fun canDisplayItem(item: CategoryDetailItem): Boolean {
        try {
            when (item.type) {
                "followCard" -> {
                    // 检查followCard是否有完整的数据结构
                    val data = item.data ?: return false
                    val content = data.content ?: return false
                    val videoData = content.data ?: return false

                    // 至少要有标题或作者信息
                    return !videoData.title.isNullOrEmpty() ||
                            !videoData.author?.name.isNullOrEmpty() ||
                            !data.header?.title.isNullOrEmpty()
                }

                "videoSmallCard" -> {
                    // 检查videoSmallCard是否有基本数据
                    val data = item.data ?: return false

                    // 至少要有标题或作者信息
                    return !data.title.isNullOrEmpty() ||
                            !data.author?.name.isNullOrEmpty()
                }

                "textCard" -> {
                    // textCard暂时不显示
                    return false
                }

                else -> {
                    // 其他类型暂时不显示
                    return false
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "检查item是否可显示时出错", e)
            return false
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

                when (item.type) {
                    "followCard" -> bindFollowCard(item, position)
                    "videoSmallCard" -> bindVideoCard(item, position)
                    else -> hideItem()
                }

                // 设置点击事件
                itemView.setOnClickListener { onItemClick(item) }
                shareButton.setOnClickListener { onShareClick(item) }

            } catch (e: Exception) {
                Log.e(TAG, "绑定数据时出现异常 - position: $position", e)
                hideItem()
            }
        }

        fun hideItem() {
            itemView.visibility = View.GONE
            val layoutParams = itemView.layoutParams
            layoutParams.height = 0
            itemView.layoutParams = layoutParams
        }

        private fun bindFollowCard(item: CategoryDetailItem, position: Int) {
            try {
                val data = item.data!!
                val content = data.content!!
                val videoData = content.data!!

                Log.d(TAG, "显示followCard - title: ${videoData.title}")

                // 设置作者信息 - 优先使用header
                val authorNameText = data.header?.title ?: videoData.author?.name ?: "未知作者"
                authorName.text = authorNameText

                // 设置作者头像
                val authorIconUrl = data.header?.icon ?: videoData.author?.icon
                loadImage(authorIconUrl, authorIcon, R.drawable.test_icon)

                // 设置时间
                val time = data.header?.time ?: videoData.date ?: videoData.releaseTime
                videoInfo.text = formatTime(time)

                // 设置视频缩略图
                loadImage(videoData.cover?.feed, videoThumbnail, R.drawable.test_img)

                // 设置描述
                val desc = videoData.descriptionEditor?.takeIf { it.isNotEmpty() }
                    ?: videoData.description?.takeIf { it.isNotEmpty() }
                    ?: videoData.title?.takeIf { it.isNotEmpty() }
                    ?: "无描述"
                description.text = desc

                // 设置互动数据
                val consumption = videoData.consumption
                likeCount.text = formatCount(consumption?.collectionCount ?: 0)
                collectCount.text = formatCount(consumption?.realCollectionCount ?: 0)
                commentCount.text = formatCount(consumption?.replyCount ?: 0)

            } catch (e: Exception) {
                Log.e(TAG, "bindFollowCard失败", e)
                hideItem()
            }
        }

        private fun bindVideoCard(item: CategoryDetailItem, position: Int) {
            try {
                val data = item.data!!

                Log.d(TAG, "显示videoCard - title: ${data.title}")

                // 设置作者信息
                authorName.text = data.author?.name ?: "未知作者"

                // 设置作者头像
                loadImage(data.author?.icon, authorIcon, R.drawable.test_icon)

                // 设置时间
                val time = data.date ?: data.releaseTime
                videoInfo.text = formatTime(time)

                // 设置视频缩略图
                loadImage(data.cover?.feed, videoThumbnail, R.drawable.test_img)

                // 设置描述
                val desc = data.descriptionEditor?.takeIf { it.isNotEmpty() }
                    ?: data.description?.takeIf { it.isNotEmpty() }
                    ?: data.title?.takeIf { it.isNotEmpty() }
                    ?: "无描述"
                description.text = desc

                // 设置互动数据
                val consumption = data.consumption
                likeCount.text = formatCount(consumption?.collectionCount ?: 0)
                collectCount.text = formatCount(consumption?.realCollectionCount ?: 0)
                commentCount.text = formatCount(consumption?.replyCount ?: 0)

            } catch (e: Exception) {
                Log.e(TAG, "bindVideoCard失败", e)
                hideItem()
            }
        }

        private fun loadImage(url: String?, imageView: ImageView, defaultRes: Int) {
            if (!url.isNullOrEmpty()) {
                // 判断是否是作者头像，如果是则使用圆形裁剪
                if (imageView == authorIcon) {
                    Glide.with(itemView.context)
                        .load(url.replace("http://", "https://"))
                        .circleCrop()  // 圆形裁剪
                        .placeholder(defaultRes)
                        .error(defaultRes)
                        .into(imageView)
                } else {
                    // 其他图片保持原样
                    Glide.with(itemView.context)
                        .load(url.replace("http://", "https://"))
                        .placeholder(defaultRes)
                        .error(defaultRes)
                        .into(imageView)
                }
            } else {
                imageView.setImageResource(defaultRes)
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
}