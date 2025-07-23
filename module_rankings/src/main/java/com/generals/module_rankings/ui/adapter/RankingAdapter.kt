package com.generals.module_rankings.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.module_rankings.R
import com.generals.module_rankings.model.bean.Item

interface RankingClickListener{
    fun onVideoClick(position: Int, item: Item)
    fun onButtonClick(position: Int, item: Item)
}

class RankingAdapter(
    private var list: MutableList<Item> = mutableListOf(),
    private val listener: RankingClickListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class RankingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val feed: ImageView
        val icon: ImageView
        val name: TextView
        val title: TextView
        val category: TextView


        init {
            // 打印 itemView 的信息
            Log.d("ViewHolder", "itemView class: ${itemView.javaClass.simpleName}")
            Log.d("ViewHolder", "itemView id: ${itemView.id}")

            // 尝试查找所有子 View
            fun findAllViews(view: View) {
                if (view is ViewGroup) {
                    for (i in 0 until view.childCount) {
                        findAllViews(view.getChildAt(i))
                    }
                } else {
                    val idName = try {
                        view.resources.getResourceEntryName(view.id)
                    } catch (e: Exception) {
                        "unknown"
                    }
                    Log.d("ViewHolder", "Found view: ${view.javaClass.simpleName} with id: $idName")
                }
            }

            findAllViews(itemView)

            // 然后正常查找
            feed = itemView.findViewById(R.id.item_video_iv)
            icon = itemView.findViewById(R.id.imageView3)
            name = itemView.findViewById(R.id.textView2)
            title = itemView.findViewById(R.id.textView)
            category = itemView.findViewById(R.id.textView3)


            Log.d("ViewHolder", "All views found successfully")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_video2, parent, false)
        Log.d("RankingAdapter", "Layout inflated successfully")
        Log.d("RankingAdapter", "View: $view")
        return RankingViewHolder(view)
    }

    fun updateData(newList: List<Item>) {
        val diffCallback = RankingDiffCallback(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        list.clear()
        list.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RankingViewHolder) {
            val item = list[position]

            // 数据绑定
            holder.name.text = item.data.author.name
            holder.category.text = item.data.category
            holder.title.text = item.data.title

            // 加载头像图片
            Glide.with(holder.itemView.context)
                .load(item.data.author.icon)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.icon)

            // 加载封面图片
            Glide.with(holder.itemView.context)
                .load(item.data.cover.feed)
                .into(holder.feed) // 封面图片通常不需要圆形裁剪
            holder.feed.setOnClickListener {
                listener?.onVideoClick(position,item)
            }


        }
    }
}