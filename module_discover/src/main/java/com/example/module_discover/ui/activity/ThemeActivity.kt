package com.example.module_discover.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.example.module_discover.R
import com.example.module_discover.model.bean.Item_detail
import com.example.module_discover.ui.adapter.NewThemeDetailAdapter
import com.example.module_discover.ui.adapter.NewThemeDetailClickListener
import com.example.module_discover.model.bean.SpecialTopicsDetailResponse
import com.example.module_discover.viewmodel.NewCategoryViewModel
import com.generals.lib.base.BaseActivity

class ThemeActivity : BaseActivity() {
    private lateinit var buttonReturn: Button
    private lateinit var buttonShare: Button
    private lateinit var textView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: NewThemeDetailAdapter
    private var currentClickedPosition = -1

    // 使用 viewModels() 委托获取 ViewModel
    private val viewModel: NewCategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme)
        postponeEnterTransition()
        // 获取传递的 ID
        val id = intent.getIntExtra("key_id", 12)
        val imageUrl = intent.getStringExtra("image_url")

        initView()
        initEvent()
        observeViewModel()

        // 加载数据
        viewModel.loadData(id)
    }

    private fun initView() {
        buttonReturn = findViewById(R.id.activity_theme_button_return)
        buttonShare = findViewById(R.id.activity_theme_button_share)
        textView = findViewById(R.id.activity_theme_textView_title)
        recyclerView = findViewById(R.id.activity_theme_recyclerview)
        progressBar = findViewById(R.id.progressBar2)
    }

    private fun initEvent() {
        // 初始化适配器
        adapter = NewThemeDetailAdapter(mutableListOf(), object : NewThemeDetailClickListener {
            override fun onItemClick(position: Int, item: SpecialTopicsDetailResponse) {
                // 处理点击事件
                when (position) {
                    0 -> {
                        // 点击了标题

                    }
                    else -> {
                        // 这里可以跳转到详情页面
                        playVideo(item)
                    }
                }
            }

            // 添加这个方法的实现
            override fun onVideoItemClick(position: Int, videoItem: Item_detail, parentItem: SpecialTopicsDetailResponse) {
                // 存储当前点击的位置
                currentClickedPosition = position
                // 调用原来的方法
                playVideo(parentItem)
            }
        })

        // 设置适配器和布局管理器
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 返回按钮点击事件
        buttonReturn.setOnClickListener {
            finish()
        }

         //分享按钮点击事件
        buttonShare.setOnClickListener {
            shareTheme()
        }
    }

    private fun playVideo(item: SpecialTopicsDetailResponse) {
        try {
            // 获取当前点击的视频项索引
            val clickedPosition = getCurrentVideoPosition() // 需要传入实际的位置

            if (clickedPosition >= 0 && clickedPosition < item.itemList.size) {
                val videoItem = item.itemList[clickedPosition]

                val id = videoItem.data.content.data.id
                val title = videoItem.data.content.data.title
                val authorName = videoItem.data.content.data.author?.name ?: ""
                val authorIcon = videoItem.data.content.data.author?.icon ?: ""
                val authorDescription = videoItem.data.content.data.author?.description ?: ""
                val subTitle = "${authorName} / #${videoItem.data.content.data.category}"
                val description = videoItem.data.content.data.description
                val collectionCount = videoItem.data.content.data.consumption?.collectionCount ?: 0
                val shareCount = videoItem.data.content.data.consumption?.shareCount ?: 0
                val replyCount = videoItem.data.content.data.consumption?.replyCount ?: 0
                val background = videoItem.data.content.data.cover?.blurred ?: ""
                val cover = videoItem.data.content.data.cover?.detail ?: ""
                val playUrl = videoItem.data.content.data.playUrl ?: ""
                val likeCount = videoItem.data.content.data.consumption?.collectionCount ?: 0

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
            }
        } catch (e: Exception) {
            Log.e("ZXY", "跳转失败", e)
            Toast.makeText(this, "跳转失败: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    private fun getCurrentVideoPosition(): Int {
        return currentClickedPosition
    }
    private fun observeViewModel() {
        // 观察数据变化
        viewModel.dataList.observe(this) { dataList ->
            dataList?.let {
                adapter.updateData(it)

                recyclerView.post {
                    val titleViewHolder = recyclerView.findViewHolderForAdapterPosition(0)
                    val topImageView = titleViewHolder?.itemView?.findViewById<ImageView>(R.id.iv_theme_cover)

                    topImageView?.transitionName = "theme_image"

                    // 简单粗暴：延迟500毫秒开始转场，给图片加载时间
                    recyclerView.postDelayed({
                        startPostponedEnterTransition()
                    }, 50)
                }
            }
        }

        // 观察加载状态
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }

        // 观察错误信息
        viewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun shareTheme() {
        // 获取当前数据
        val currentData = viewModel.dataList.value

        if (!currentData.isNullOrEmpty()) {
            val firstItem = currentData[0]
            val shareText = "分享主题: ${firstItem.brief}\n${firstItem.text}\n${firstItem.shareLink ?: ""}"

            // 创建分享Intent
            val shareIntent = android.content.Intent().apply {
                action = android.content.Intent.ACTION_SEND
                type = "text/plain"
                putExtra(android.content.Intent.EXTRA_TEXT, shareText)
                putExtra(android.content.Intent.EXTRA_SUBJECT, "主题分享")
            }

            // 启动分享选择器
            startActivity(android.content.Intent.createChooser(shareIntent, "分享到"))
        } else {
            Toast.makeText(this, "暂无可分享的内容", Toast.LENGTH_SHORT).show()
        }
    }
}