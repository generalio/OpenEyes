package com.example.module_discover.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.module_discover.R
import com.example.module_discover.model.bean.Item
import com.example.module_discover.model.bean.Item_detail
import com.example.module_discover.model.bean.SpecialTopicsDetailResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface NewThemeDetailClickListener {
    fun onItemClick(position: Int, item: SpecialTopicsDetailResponse)
    fun onVideoItemClick(position: Int, videoItem: Item_detail, parentItem: SpecialTopicsDetailResponse) // 添加 position 参数
}
class NewThemeDetailAdapter(
    private var list: MutableList<SpecialTopicsDetailResponse> = mutableListOf(),
    private var listener: NewThemeDetailClickListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_TITLE = 0
        const val TYPE_VIDEO = 1
        const val TYPE_END = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TITLE -> TitleViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.activity_theme_title, parent, false)
            )
            TYPE_VIDEO -> VideoViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.activity_theme_video, parent, false)
            )
            TYPE_END -> EndViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.layout_end, parent, false)
            )
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun getItemCount(): Int {
        // 如果没有数据，只显示标题和结尾
        return if (list.isEmpty()) 2 else list[0].itemList.size + 2
    }

    // 修正：接收 List 类型的数据
    fun updateData(newList: List<SpecialTopicsDetailResponse>) {
        this.list.clear()
        this.list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list.isEmpty()) return

        when (holder.itemViewType) {
            TYPE_TITLE -> {
                val titleHolder = holder as TitleViewHolder
                titleHolder.bind(list[0], listener)
            }
            TYPE_VIDEO -> {
                val videoHolder = holder as VideoViewHolder
                val actualIndex = position - 1
                // 确保索引在有效范围内
                if (actualIndex >= 0 && actualIndex < list[0].itemList.size) {
                    videoHolder.bind(list[0], actualIndex, listener)
                }
            }
            TYPE_END -> {
                // End 布局不需要绑定数据
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TYPE_TITLE
            list.isNotEmpty() && position == list[0].itemList.size + 1 -> TYPE_END
            else -> TYPE_VIDEO
        }
    }
}

class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView = itemView.findViewById(R.id.iv_theme_cover)
    private val textView: TextView = itemView.findViewById(R.id.tv_theme_title)
    private val subTextView: TextView = itemView.findViewById(R.id.tv_theme_desc)

    fun bind(item: SpecialTopicsDetailResponse, listener: NewThemeDetailClickListener?) {
        textView.text = item.brief
        subTextView.text = item.text
        Glide.with(itemView.context).load(item.headerImage).apply(RequestOptions().centerCrop()).into(imageView)

        itemView.setOnClickListener {
            listener?.onItemClick(adapterPosition, item)
        }
    }
}

class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val iconImageView: ImageView = itemView.findViewById(R.id.iv_post_icon)
    private val iconNameTextView: TextView = itemView.findViewById(R.id.tv_post_title)
    private val timeTextView: TextView = itemView.findViewById(R.id.tv_publish_date)
    private val titleTextView: TextView = itemView.findViewById(R.id.tv_post_subtitle)
    private val subTitleTextView: TextView = itemView.findViewById(R.id.tv_post_content)
    private val tag1TextView: TextView = itemView.findViewById(R.id.tv_tag_inspiration)
    private val tag2TextView: TextView = itemView.findViewById(R.id.tv_tag_retro)
    private val tag3TextView: TextView = itemView.findViewById(R.id.tv_tag_beautiful)
    private val videoImageView: ImageView = itemView.findViewById(R.id.iv_video_thumbnail)

    fun bind(item: SpecialTopicsDetailResponse, itemIndex: Int, listener: NewThemeDetailClickListener?) {
        // 安全地访问 itemList
        if (itemIndex >= 0 && itemIndex < item.itemList.size) {
            val currentItem = item.itemList[itemIndex]

            iconNameTextView.text = currentItem.data.header.issuerName
            timeTextView.text = formatTime(currentItem.data.header.time)
            titleTextView.text = currentItem.data.content.data.title
            subTitleTextView.text = currentItem.data.content.data.description

            val tags = currentItem.data.content.data.tags
            if (tags.size >= 3) {
                tag1TextView.text = tags[0].name
                tag2TextView.text = tags[1].name
                tag3TextView.text = tags[2].name
            }

            Glide.with(itemView.context)
                .load(currentItem.data.header.icon)
                .apply(RequestOptions.circleCropTransform())
                .into(iconImageView)

            Glide.with(itemView.context)

                .load(currentItem.data.content.data.cover.feed)
                .into(videoImageView)

            videoImageView.setOnClickListener {
                val currentItem = item.itemList[itemIndex]
                // 传递实际的位置 (adapterPosition 是在 RecyclerView 中的位置，需要减1因为第一位是标题)
                val videoPosition = adapterPosition - 1
                listener?.onVideoItemClick(videoPosition, currentItem, item)
            }
        }
    }
}

class EndViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    init {
        val imageView: ImageView = itemView.findViewById(R.id.imageView7)
        imageView.setImageResource(R.drawable.end)
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