import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.module_discover.R
import com.example.module_discover.model.bean.ImageItem
import com.example.module_discover.model.bean.ThemeDetailItem
import com.example.module_discover.model.bean.TitleItem
import com.example.module_discover.model.bean.VideoItem

interface ThemeDetailClickListener {
    fun onItemClick(position: Int, item: ThemeDetailItem)
}

class ThemeDetailAdapter(
    private var list: List<ThemeDetailItem>,
    private var listener: ThemeDetailClickListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_TITLE = 0
        const val TYPE_VIDEO = 1
        const val TYPE_END = 2
    }

    override fun getItemViewType(position: Int): Int {
        // 如果是最后一个位置，显示 end 布局
        return if (position == list.size) {
            TYPE_END
        } else {
            when (list[position]) {
                is TitleItem -> TYPE_TITLE
                is VideoItem -> TYPE_VIDEO
                else -> throw IllegalArgumentException("Unknown item type")
            }
        }
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TitleViewHolder -> holder.bind(list[position] as TitleItem, listener)
            is VideoViewHolder -> holder.bind(list[position] as VideoItem, listener)
            is EndViewHolder -> {
                // End 布局不需要绑定数据，什么都不做
            }
        }
    }

    override fun getItemCount(): Int = list.size + 1

    // 更新数据的方法
    fun updateData(newList: List<ThemeDetailItem>) {
        list = newList
        notifyDataSetChanged()
    }
}

class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView = itemView.findViewById(R.id.iv_theme_cover)
    private val textView: TextView = itemView.findViewById(R.id.tv_theme_title)
    private val subTextView: TextView = itemView.findViewById(R.id.tv_theme_desc)

    fun bind(item: TitleItem, listener: ThemeDetailClickListener?) {
        textView.text = item.title
        subTextView.text = item.subTitle
        Glide.with(itemView.context).load(item.imageTitle).into(imageView)

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

    fun bind(item: VideoItem, listener: ThemeDetailClickListener?) {
        iconNameTextView.text = item.iconName
        timeTextView.text = item.time
        titleTextView.text = item.title
        subTitleTextView.text = item.subTitle
        tag1TextView.text = item.tag1
        tag2TextView.text = item.tag2
        tag3TextView.text = item.tag3

        Glide.with(itemView.context).load(item.icon).apply(RequestOptions.circleCropTransform()).into(iconImageView)
        Glide.with(itemView.context).load(item.videoImage).into(videoImageView)

        itemView.setOnClickListener {
            listener?.onItemClick(adapterPosition, item)
        }
    }
}

class EndViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // 构造函数中就设置好图片，不需要 bind 方法
    init {
        val imageView: ImageView = itemView.findViewById(R.id.imageView7)
        imageView.setImageResource(R.drawable.end)
    }
}