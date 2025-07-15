package com.example.module_discover.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.module_discover.R
import com.example.module_discover.ui.adapter.CategoryAdapter
import com.example.module_discover.model.bean.CategoryItem
import com.example.module_discover.model.bean.ThemeItem
import com.example.module_discover.ui.adapter.OnCategoryItemClickListener
import com.example.module_discover.ui.adapter.ThemeAdapter
import com.example.module_discover.ui.adapter.ThemeItemClickListener
import com.example.module_discover.viewmodel.DiscoverViewModel

class DiscoverFragment : Fragment() {
    private lateinit var first_recyclerView: RecyclerView
    private lateinit var Second_recyclerView: RecyclerView
    private lateinit var cardView: CardView
    private lateinit var button: Button
    private lateinit var viewModel: DiscoverViewModel
    private lateinit var themeAdapter: ThemeAdapter

    private val categoryList = listOf(
        CategoryItem(R.drawable.advertising, "广告", 14),
        CategoryItem(R.drawable.exercise, "运动", 18),
        CategoryItem(R.drawable.music, "音乐", 20),
        CategoryItem(R.drawable.plot, "剧情", 12),
        CategoryItem(R.drawable.funny, "搞笑", 28),
        CategoryItem(R.drawable.appetizing, "开胃", 4),
        CategoryItem(R.drawable.video, "综艺", 38),
        CategoryItem(R.drawable.fashion, "时尚", 24),
        CategoryItem(R.drawable.life, "生活", 36),
        CategoryItem(R.drawable.cartoon, "动画", 10),
        CategoryItem(R.drawable.game, "游戏", 30),
        CategoryItem(R.drawable.movie, "影视", 8),
        CategoryItem(R.drawable.record, "记录", 22),
        CategoryItem(R.drawable.creativity, "创意", 2),
        CategoryItem(R.drawable.science, "科技", 32),
        CategoryItem(R.drawable.travel, "旅行", 6),
        CategoryItem(R.drawable.inform, "汽车", 34),
        CategoryItem(R.drawable.car, "萌宠", 26),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_discover, container, false)
        viewModel = ViewModelProvider(this)[DiscoverViewModel::class.java]

        if (view != null) {
            initView(view)
            setupObservers()
        } else {
            Log.e("DiscoverFragment", "视图创建失败，view 为 null")
        }

        // 加载数据
        viewModel.loadThemeItemData()
        viewModel.loadAllData()

        return view
    }

    private fun initView(view: View) {
        button = view.findViewById(R.id.button2)
        cardView = view.findViewById(R.id.search_Container)
        first_recyclerView = view.findViewById(R.id.category_recyclerView)
        Second_recyclerView = view.findViewById(R.id.theme_playlist_recyclerView)

        // 初始化 ThemeAdapter，传入空列表
        themeAdapter = ThemeAdapter(emptyList(), object : ThemeItemClickListener {
            override fun onItemClick(position: Int, item: ThemeItem) {
                val message = "点击了 ${item.title}，ID: ${item.id}"
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        })

        // 设置水平滑动的 LinearLayoutManager
        val horizontalLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        Second_recyclerView.layoutManager = horizontalLayoutManager
        Second_recyclerView.adapter = themeAdapter

        // 添加水平滑动的优化设置
        Second_recyclerView.setHasFixedSize(true)
        Second_recyclerView.isNestedScrollingEnabled = false

        val gridLayoutManager = GridLayoutManager(requireContext(), 3) // 3列网格
        first_recyclerView.layoutManager = gridLayoutManager
        first_recyclerView.adapter = CategoryAdapter(categoryList, object : OnCategoryItemClickListener {
            override fun onItemClick(position: Int, item: CategoryItem) {
                val message = "点击了 ${item.CategoryName}，ID: ${item.id}"
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        })

        // 添加日志验证
        Log.d("DiscoverFragment", "RecyclerView 初始化完成，数据数量: ${categoryList.size}")
    }

    private fun setupObservers() {
        // 观察主题数据变化
        viewModel.themeList.observe(viewLifecycleOwner) { themeList ->
            themeList?.let {
                // 更新适配器数据
                themeAdapter.updateData(it)
                Log.d("DiscoverFragment", "ThemeList 数据更新，数量: ${it.size}")
            }
        }

        // 观察加载状态
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            // 可以在这里显示/隐藏加载指示器
            Log.d("DiscoverFragment", "加载状态: $isLoading")
        }

        // 观察错误信息
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                Log.e("DiscoverFragment", "错误信息: $it")
            }
        }
    }
}