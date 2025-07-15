package com.example.module_discover.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.module_discover.R
import com.example.module_discover.model.bean.ThemeItem

// 定义点击事件接口
interface ThemeItemClickListener {
    fun onItemClick(position: Int, item: ThemeItem)
}

class ThemeAdapter(
    private var list: List<ThemeItem>,
    private val listener: ThemeItemClickListener? = null
) : RecyclerView.Adapter<ThemeAdapter.ThemeHolder>() {

    class ThemeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.theme_playlist_rv_item_image)
        val description: TextView = itemView.findViewById(R.id.theme_playlist_rv_item_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.theme_playlist_rv_item, parent, false)
        return ThemeHolder(view)
    }

    override fun onBindViewHolder(holder: ThemeHolder, position: Int) {
        val item = list[position]

        holder.description.text = item.title
        loadImage(holder.imageView, item.imageUrl)

        // 绑定点击事件
        holder.itemView.setOnClickListener {
            listener?.onItemClick(position, item)
        }
    }

    private fun loadImage(imageView: ImageView, imageUrl: String) {
        if (imageUrl.isEmpty()) {
            // 如果图片URL为空，显示默认图片
            imageView.setImageResource(R.drawable.car)
            return
        }

        // 使用 Glide 加载网络图片
        Glide.with(imageView.context)
            .load(imageUrl)
            .apply(
                RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .placeholder(R.drawable.car) // 添加占位图
                    .error(R.drawable.car) // 添加错误图片
            )
            .into(imageView)
    }

    override fun getItemCount(): Int = list.size

    // 添加更新数据的方法
    fun updateData(newList: List<ThemeItem>) {
        list = newList
        notifyDataSetChanged()
    }
}