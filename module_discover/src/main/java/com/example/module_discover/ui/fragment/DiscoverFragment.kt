package com.example.module_discover.ui.fragment

import MainItem
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.module_discover.R
import com.example.module_discover.ui.adapter.CategoryAdapter
import com.example.module_discover.model.bean.CategoryItem
import com.example.module_discover.model.bean.ThemeItem
import com.example.module_discover.ui.activity.CategoryDetailActivity
import com.example.module_discover.ui.activity.ThemeActivity
import com.example.module_discover.ui.adapter.NewDiscoverAdapter
import com.example.module_discover.ui.adapter.OnCategoryItemClickListener
import com.example.module_discover.ui.adapter.ThemeAdapter
import com.example.module_discover.ui.adapter.ThemeItemClickListener
import com.example.module_discover.viewmodel.DiscoverViewModel

class DiscoverFragment : Fragment() {
    private lateinit var first_recyclerView: RecyclerView
    private lateinit var Second_recyclerView: RecyclerView
    private lateinit var cardView: CardView
    private lateinit var mainRecyclerView:RecyclerView
    private lateinit var viewModel: DiscoverViewModel
    private lateinit var themeAdapter: ThemeAdapter
    private lateinit var mainAdapter: NewDiscoverAdapter

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

    private val defaultThemeList = listOf(
        ThemeItem(0, "加载中...", "默认描述1"),
        ThemeItem(1, "加载中...", "默认描述2"),
        ThemeItem(2, "加载中...", "默认描述3"),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.new_fragment_discover, container, false)
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
        mainRecyclerView = view.findViewById(R.id.new_discover_rv)

        // 初始化主 Adapter
        mainAdapter = NewDiscoverAdapter(
            list = createInitialSections(),
            categoryClickListener = object : OnCategoryItemClickListener {
                override fun onItemClick(position: Int, item: CategoryItem) {
                    val intent = Intent(requireContext(), CategoryDetailActivity::class.java)
                    intent.putExtra("key_id", item.id)
                    intent.putExtra("key_name", item.CategoryName)
                    startActivity(intent)
                }
            },
            themeClickListener = object : ThemeItemClickListener {
                override fun onItemClick(position: Int, item: ThemeItem, sharedElement: View) {
                    val intent = Intent(requireContext(), ThemeActivity::class.java)
                    intent.putExtra("key_id", item.id)
                    intent.putExtra("image_url", item.imageUrl)

                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        requireActivity(),
                        sharedElement,
                        "theme_image"
                    )

                    startActivity(intent, options.toBundle())
                }
            }
        )

        // 设置主 RecyclerView
        mainRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mainRecyclerView.adapter = mainAdapter
        mainRecyclerView.setHasFixedSize(true)
    }
    private fun createInitialSections(): List<MainItem> {
        return listOf(
            MainItem(
                title = "热门分类",
                categoryList = categoryList,
                themeList = null,
                sectionType = SectionType.CATEGORY
            ),
            MainItem(
                title = "推荐主题",
                categoryList = null,
                themeList = defaultThemeList,
                sectionType = SectionType.THEME
            )
        )
    }


    private fun setupObservers() {
        // 观察主题数据变化
        viewModel.themeList.observe(viewLifecycleOwner) { themeList ->
            themeList?.let {
                // 更新主题部分的数据
                val updatedSections = listOf(
                    MainItem(
                        title = "热门分类",
                        categoryList = categoryList,
                        themeList = null,
                        sectionType = SectionType.CATEGORY
                    ),
                    MainItem(
                        title = "推荐主题",
                        categoryList = null,
                        themeList = it,
                        sectionType = SectionType.THEME
                    )
                )
                mainAdapter.updateData(updatedSections)
                Log.d("DiscoverFragment", "ThemeList 数据更新，数量: ${it.size}")
            }
        }
    }
}