package com.example.module_discover.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.example.module_discover.R
import com.example.module_discover.ui.adapter.CategoryDisplayAdapter
import com.example.module_discover.viewmodel.CategoryDetailViewModel
import com.generals.lib.base.BaseActivity
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.example.module_discover.model.bean.CategoryDetailItem


class CategoryDetailActivity : BaseActivity() {
    private lateinit var imageView: ImageView
    private lateinit var toolbar: Toolbar
    private lateinit var textView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout

    private lateinit var adapter: CategoryDisplayAdapter
    private val viewModel: CategoryDetailViewModel by viewModels()

    private var categoryId: Int = 0


    companion object {
        private const val TAG = "CategoryDetailActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_detail)

        categoryId = intent.getIntExtra("key_id", 0)

        Log.d(TAG, "onCreate: categoryId = $categoryId")

        // 检查categoryId是否有效
        if (categoryId == 0) {
            Log.e(TAG, "无效的categoryId: $categoryId")
            Toast.makeText(this, "无效的分类ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        initView()
        initEvent()
        setupObservers()
        loadData()
    }

    private fun initView() {
        Log.d(TAG, "initView: 开始初始化视图")

        imageView = findViewById(R.id.img_detail)
        toolbar = findViewById(R.id.toolbar_detail)
        //textView = findViewById(R.id.text_detail)
        recyclerView = findViewById(R.id.rv_detail)
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_detail)
        SetImage(categoryId)

        setupRecyclerView()
        Log.d(TAG, "initView: 视图初始化完成")
    }

    private fun SetImage(categoryId: Int) {
        when(categoryId){

        }
    }

    private fun setupRecyclerView() {
        Log.d(TAG, "setupRecyclerView: 开始设置RecyclerView")

        adapter = CategoryDisplayAdapter(
            onItemClick = { item ->
                Log.d(TAG, "点击视频: ${item.data.title}")

                playVideo(item)
            },
            onShareClick = { item ->
                Log.d(TAG, "分享: ${item.data.title}")
                Toast.makeText(this, "分享: ${item.data.title}", Toast.LENGTH_SHORT).show()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        Log.d(TAG, "setupRecyclerView: RecyclerView设置完成")
    }

    private fun playVideo(item: CategoryDetailItem) {
        try {
            val id = item.data.content.data.id
            val title = item.data.content.data.title
            val authorName = item.data.content.data.author.name
            val authorIcon = item.data.content.data.author.icon
            val authorDescription = item.data.content.data.author.description
            val subTitle = (item.data.content.data.author.name ) + " / #" + (item.data.content.data.category )
            val description = item.data.content.data.description
            val collectionCount = item.data.content.data.consumption.collectionCount
            val shareCount = item.data.content.data.consumption.shareCount
            val replyCount = item.data.content.data.consumption.replyCount
            val background = item.data.content.data.cover.blurred
            val cover = item.data.content.data.cover.detail
            val playUrl = item.data.content.data.playUrl
            val likeCount = item.data.content.data.consumption.collectionCount

            Log.d("ZXY", subTitle)

            ARouter.getInstance().build("/video/VideoActivity")
                .withInt("id", id)
                .withString("title", title)
                .withString("subTitle", subTitle)
                .withString("description", description)
                .withInt("collectionCount", collectionCount)
                .withInt("shareCount", shareCount)
                .withInt("replyCount", replyCount)
                .withString("background", background)
                .withString("cover", cover)
                .withString("playUrl", playUrl)
                .withString("authorName", authorName)
                .withString("authorIcon", authorIcon)
                .withString("authorDescription", authorDescription)
                .withInt("likeCount", likeCount)
                .navigation()

        } catch (e: Exception) {
            Log.e("ZXY", "跳转失败", e)
        }

    }


    private fun setupObservers() {
        Log.d(TAG, "setupObservers: 开始设置观察者")

        // 观察分页数据 - 替换原来的LiveData观察
        lifecycleScope.launch {
            viewModel.getCategoryDetail(categoryId).collectLatest { pagingData ->
                Log.d(TAG, "收到新的分页数据")
                adapter.submitData(pagingData)
            }
        }

        // 观察加载状态 - 使用PagingDataAdapter的loadStateFlow
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadState ->
                // 处理刷新状态
                val isLoading = loadState.refresh is LoadState.Loading
                Log.d(TAG, "加载状态: $isLoading")

                // 可以在这里显示/隐藏加载指示器
                // progressBar.isVisible = isLoading

                // 处理错误状态
                val errorState = loadState.refresh as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error

                errorState?.let { error ->
                    Log.e(TAG, "分页加载错误: ${error.error.message}")
                    Toast.makeText(this@CategoryDetailActivity,
                        "加载失败: ${error.error.message}",
                        Toast.LENGTH_LONG).show()
                }
            }
        }

        // 如果ViewModel中还有其他LiveData，保留观察
        /*
        viewModel.loading.observe(this) { isLoading ->
            Log.d(TAG, "额外加载状态: $isLoading")
        }

        viewModel.error.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Log.e(TAG, "额外错误信息: $errorMessage")
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
        */

        Log.d(TAG, "setupObservers: 观察者设置完成")
    }

    private fun initEvent() {
        Log.d(TAG, "initEvent: 开始设置事件")


        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 设置标题和描述
        val categoryName = intent.getStringExtra("key_name") ?: "分类详情"
        //val categoryDescription = intent.getStringExtra("key_description") ?: ""

        collapsingToolbarLayout.title = categoryName
        //textView.text = categoryDescription.ifEmpty { categoryName }

        // 设置背景图片
        val categoryImage = intent.getStringExtra("key_image")
        if (!categoryImage.isNullOrEmpty()) {
            Glide.with(this)
                .load(categoryImage)
                .placeholder(R.drawable.test_img)
                .error(R.drawable.test_img)
                .into(imageView)
        } else {
            imageView.setImageResource(R.drawable.img_common)
        }

        Log.d(TAG, "initEvent: 事件设置完成")
    }

    private fun loadData() {
        Log.d(TAG, "loadData: 开始加载数据，categoryId = $categoryId")

        // 使用Paging 3时，数据加载是自动的，不需要手动调用
        // viewModel.loadCategoryDetailData(categoryId) // 删除这行

        // 数据会通过 setupObservers() 中的 collectLatest 自动加载和更新
        Log.d(TAG, "loadData: Paging 3 会自动处理数据加载")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}