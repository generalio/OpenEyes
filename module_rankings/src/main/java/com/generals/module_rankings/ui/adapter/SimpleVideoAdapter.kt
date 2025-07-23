package com.generals.module_rankings.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.module_rankings.R
import com.generals.module_rankings.model.bean.Item
import com.generals.module_rankings.model.bean.VideoType
class SimpleVideoAdapter(
    private val exoPlayer: ExoPlayer,
    private val onItemClick: (Any) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Any> = emptyList()

    companion object {
        const val TYPE_VIDEO = 0
        private const val TYPE_CONTENT = 1
    }

    fun updateList(newItems: List<Any>) {
        val oldItems = items
        items = newItems

        // 如果新列表中没有VideoType，说明视频被移除了
        val hadVideo = oldItems.any { it is VideoType }
        val hasVideo = newItems.any { it is VideoType }

        if (hadVideo && !hasVideo) {
            exoPlayer.pause()
            exoPlayer.stop()
            exoPlayer.clearMediaItems()
        }

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is VideoType -> TYPE_VIDEO
            is Item -> TYPE_CONTENT
            else -> TYPE_CONTENT
        }
    }
    @UnstableApi
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_VIDEO -> VideoViewHolder.create(parent, exoPlayer)
            TYPE_CONTENT -> ContentViewHolder.create(parent, onItemClick)
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }
    @UnstableApi
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is VideoViewHolder -> holder.bind((items[position] as VideoType).item)
            is ContentViewHolder -> holder.bind(items[position] as Item)
        }
    }

    override fun getItemCount() = items.size
}
@UnstableApi
// ViewHolder实现 (去掉HeaderViewHolder)
class VideoViewHolder(
    itemView: View,
    private val exoPlayer: ExoPlayer
) : RecyclerView.ViewHolder(itemView) {
    private val playerView: PlayerView = itemView.findViewById(R.id.playerView)
    private val titleText: TextView = itemView.findViewById(R.id.titleText)

    init {
        setupPlayerView()
        setDefaultHeight()
    }
    @UnstableApi
    private fun setupPlayerView() {
        playerView.apply {
            player = exoPlayer
            useController = false
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        }
    }

    private fun setDefaultHeight() {
        val screenWidth = itemView.context.resources.displayMetrics.widthPixels
        val defaultHeight = (screenWidth * 9f / 16f).toInt()
        itemView.layoutParams.height = defaultHeight
        itemView.requestLayout()
    }

    fun bind(item: Item) {
        titleText.text = item.data.title
    }

    companion object {
        fun create(parent: ViewGroup, exoPlayer: ExoPlayer): VideoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_video3, parent, false)
            return VideoViewHolder(view, exoPlayer)
        }
    }
}

class ContentViewHolder(
    itemView: View,
    private val onItemClick: (Any) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val feed: ImageView = itemView.findViewById(R.id.item_video_iv)
    private val icon: ImageView = itemView.findViewById(R.id.imageView3)
    private val name: TextView = itemView.findViewById(R.id.textView2)
    private val title: TextView = itemView.findViewById(R.id.textView)
    private val category: TextView = itemView.findViewById(R.id.textView3)

    fun bind(item: Item) {
        title.text = item.data.title
        name.text = item.data.author.name
        category.text = "#${item.data.category}"

        // 加载头像图片
        Glide.with(itemView.context)
            .load(item.data.author.icon)
            .apply(RequestOptions.circleCropTransform())
            .into(icon)

        // 加载封面图片
        Glide.with(itemView.context)
            .load(item.data.cover.feed)
            .into(feed)

        itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    companion object {
        fun create(parent: ViewGroup, onItemClick: (Any) -> Unit): ContentViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_video2, parent, false)
            return ContentViewHolder(view, onItemClick)
        }
    }
}
