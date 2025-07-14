package com.example.module_discover

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.module_discover.adapter.CategoryAdapter
import com.example.module_discover.network.bean.CategoryItem

class DiscoverFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cardView: CardView
    private lateinit var button: Button

    private val categoryList = listOf(
        CategoryItem(R.drawable.inform, "通知", 15),
        CategoryItem(R.drawable.inform, "消息", 15),
        CategoryItem(R.drawable.inform, "设置", 15),
        CategoryItem(R.drawable.inform, "帮助", 15),
        CategoryItem(R.drawable.inform, "关于", 15),
        CategoryItem(R.drawable.inform, "反馈", 15),
        CategoryItem(R.drawable.inform, "分享", 15),
        CategoryItem(R.drawable.inform, "收藏", 15),
        CategoryItem(R.drawable.inform, "历史", 15),
        CategoryItem(R.drawable.inform, "搜索", 15),
        CategoryItem(R.drawable.inform, "更多", 15),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_discover, container, false)

        if (view != null) {
            initView(view)
        } else {
            Log.e("DiscoverFragment", "视图创建失败，view 为 null")
        }

        // 只返回一次视图
        return view
    }

    private fun initView(view: View) {
        button = view.findViewById(R.id.button2)
        cardView = view.findViewById(R.id.search_Container)
        recyclerView = view.findViewById(R.id.recyclerView)

        val gridLayoutManager = GridLayoutManager(requireContext(), 3) // 3列网格
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = CategoryAdapter(categoryList)

        // 添加日志验证
        Log.d("DiscoverFragment", "RecyclerView 初始化完成，数据数量: ${categoryList.size}")
    }
}