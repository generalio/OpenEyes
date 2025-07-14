package com.example.module_discover.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.module_discover.R
import com.example.module_discover.network.bean.CategoryItem

class CategoryAdapter(private val list: List<CategoryItem>):
    RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {

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
        holder.imageView.setImageResource(item.imageResId)
        // 确保属性名称正确，根据你的CategoryItem类定义
        holder.imageName.text = item.CategoryName // 或者 item.CategoryName，取决于你的数据类定义
    }

    override fun getItemCount(): Int = list.size
}