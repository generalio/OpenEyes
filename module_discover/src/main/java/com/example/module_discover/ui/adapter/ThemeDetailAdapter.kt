import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.module_discover.R
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
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is TitleItem -> TYPE_TITLE
            is VideoItem -> TYPE_VIDEO
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
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TitleViewHolder -> holder.bind(list[position] as TitleItem, listener)
            is VideoViewHolder -> holder.bind(list[position] as VideoItem, listener)
        }
    }

    override fun getItemCount(): Int = list.size

    // 更新数据的方法
    fun updateData(newList: List<ThemeDetailItem>) {
        list = newList
        notifyDataSetChanged()
    }
}

// 3. 修正后的ViewHolder
class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView = itemView.findViewById(R.id.iv_theme_cover)
    private val textView: TextView = itemView.findViewById(R.id.tv_theme_title)
    private val subTextView: TextView = itemView.findViewById(R.id.tv_theme_desc)

    fun bind(item: TitleItem, listener: ThemeDetailClickListener?) {
        // 绑定数据
        textView.text = item.title
        subTextView.text = item.subTitle

        // 加载图片 - 使用你项目中的图片加载库，比如Glide或Picasso
        Glide.with(itemView.context).load(item.imageTitle).into(imageView)

        // 设置点击事件
        itemView.setOnClickListener {
            listener?.onItemClick(adapterPosition, item)
        }
    }
}

class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // 根据你的布局文件修改这些ID
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
        // 绑定数据

        iconNameTextView.text = item.iconName
        timeTextView.text = item.time
        titleTextView.text = item.title
        subTitleTextView.text = item.subTitle
        tag1TextView.text = item.tag1
        tag2TextView.text = item.tag2
        tag3TextView.text = item.tag3

        // 加载图片
        Glide.with(itemView.context).load(item.icon).apply(RequestOptions.circleCropTransform()).into(iconImageView)
        Glide.with(itemView.context).load(item.videoImage).into(videoImageView)

        // 设置点击事件
        itemView.setOnClickListener {
            listener?.onItemClick(adapterPosition, item)
        }
    }
}