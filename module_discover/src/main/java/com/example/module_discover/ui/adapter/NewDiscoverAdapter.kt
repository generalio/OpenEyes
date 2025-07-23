package com.example.module_discover.ui.adapter

import MainItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.module_discover.R
import com.example.module_discover.model.bean.CategoryItem
import com.example.module_discover.model.bean.ThemeItem

class NewDiscoverAdapter(
    private var list: List<MainItem>,  // 修复：使用 var 以便更新
    private val categoryClickListener: OnCategoryItemClickListener,
    private val themeClickListener: ThemeItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_CATEGORY = 0
        private const val TYPE_THEME = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position].sectionType) {
            SectionType.CATEGORY -> TYPE_CATEGORY
            SectionType.THEME -> TYPE_THEME
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CATEGORY -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_category, parent, false)
                CategorySectionViewHolder(view)
            }
            TYPE_THEME -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_theme, parent, false)
                ThemeSectionViewHolder(view)
            }
            else -> throw IllegalArgumentException("未知的 ViewType: $viewType")
        }
    }

    override fun getItemCount(): Int = list.size

    // 修复：更新数据方法
    fun updateData(newItems: List<MainItem>) {
        list = newItems
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val section = list[position]  // 修复：使用 list 而不是 sections
        when (holder) {
            is CategorySectionViewHolder -> {
                section.categoryList?.let { holder.bind(section.title, it) }
            }
            is ThemeSectionViewHolder -> {
                section.themeList?.let { holder.bind(section.title, it) }
            }
        }
    }

    inner class CategorySectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.category_title1)
        private val categoryRecyclerView: RecyclerView = itemView.findViewById(R.id.category_recyclerView1)

        fun bind(title: String, categoryList: List<CategoryItem>) {
            titleText.text = title

            // 设置分类网格布局
            categoryRecyclerView.layoutManager = GridLayoutManager(itemView.context, 3)
            categoryRecyclerView.adapter = CategoryAdapter(categoryList, categoryClickListener)
            categoryRecyclerView.setHasFixedSize(true)
            categoryRecyclerView.isNestedScrollingEnabled = false
        }
    }

    inner class ThemeSectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.theme_playlist_textView1)
        private val themeRecyclerView: RecyclerView = itemView.findViewById(R.id.theme_playlist_recyclerView1)

        fun bind(title: String, themeList: List<ThemeItem>) {
            titleText.text = title

            // 设置水平滑动布局
            val horizontalLayoutManager = LinearLayoutManager(
                itemView.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            themeRecyclerView.layoutManager = horizontalLayoutManager
            themeRecyclerView.adapter = ThemeAdapter(themeList, themeClickListener)
            themeRecyclerView.setHasFixedSize(true)
            themeRecyclerView.isNestedScrollingEnabled = false
        }
    }
}