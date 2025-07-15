package com.example.module_discover.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.module_discover.R
import com.example.module_discover.model.bean.CategoryItem

// 定义点击事件接口
interface OnCategoryItemClickListener {
    fun onItemClick(position: Int, item: CategoryItem)
}

class CategoryAdapter(
    private val list: List<CategoryItem>,
    private val listener: OnCategoryItemClickListener? = null
) : RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {

    class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView2)
        val imageName: TextView = itemView.findViewById(R.id.classify_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.classify_item, parent, false)
        return CategoryHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val item = list[position]
        // 设置图标和文字
        holder.imageView.setImageResource(item.imageResId)
        holder.imageName.text = item.CategoryName // 修正属性名首字母小写（规范命名）

        // 绑定点击事件
        holder.itemView.setOnClickListener {
            // 回调接口方法，传递位置和数据
            listener?.onItemClick(position, item)
        }

    }

    override fun getItemCount(): Int = list.size
}