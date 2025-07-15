package com.example.module_discover.ui.activity

import ThemeDetailAdapter
import ThemeDetailClickListener
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.module_discover.R
import com.example.module_discover.model.bean.ThemeDetailItem
import com.example.module_discover.model.bean.TitleItem
import com.example.module_discover.model.bean.VideoItem
import com.example.module_discover.viewmodel.DiscoverViewModel
import com.generals.lib.base.BaseActivity

class ThemeActivity : BaseActivity() {
    private lateinit var buttonReturn: Button
    private lateinit var buttonShare: Button
    private lateinit var textView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var themeDetailAdapter: ThemeDetailAdapter

    // 使用 viewModels() 委托获取 ViewModel
    private val viewModel: DiscoverViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme)

        // 获取传递的 ID
        val id = intent.getIntExtra("key_id", 0)

        initView()
        initEvent()
        observeViewModel()

        // 加载数据
        if (id != 0) {
            viewModel.loadThemeDetailItemData(id)
        } else {
            Toast.makeText(this, "无效的主题ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView() {
        buttonReturn = findViewById(R.id.activity_theme_button_return)
        buttonShare = findViewById(R.id.activity_theme_button_share)
        textView = findViewById(R.id.activity_theme_textView_title)
        recyclerView = findViewById(R.id.activity_theme_recyclerview)
    }

    private fun initEvent() {
        // 初始化适配器
        themeDetailAdapter = ThemeDetailAdapter(emptyList(), object : ThemeDetailClickListener {
            override fun onItemClick(position: Int, item: ThemeDetailItem) {
                when (item) {
                    is TitleItem -> {
                        // 处理标题点击
                        Toast.makeText(this@ThemeActivity, "点击了标题: ${item.title}", Toast.LENGTH_SHORT).show()
                    }
                    is VideoItem -> {
                        // 处理视频点击
                        Toast.makeText(this@ThemeActivity, "点击了视频: ${item.title}", Toast.LENGTH_SHORT).show()
                        // 这里可以跳转到视频播放页面
                        // val intent = Intent(this@ThemeActivity, VideoPlayerActivity::class.java)
                        // intent.putExtra("video_url", item.videoUrl)
                        // startActivity(intent)
                    }
                }
            }
        })

        // 设置适配器和布局管理器
        recyclerView.adapter = themeDetailAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 返回按钮点击事件
        buttonReturn.setOnClickListener {
            finish()
        }

        // 分享按钮点击事件
        buttonShare.setOnClickListener {
            // 实现分享功能
            shareTheme()
        }
    }

    private fun observeViewModel() {
        // 观察主题详情数据
        viewModel.themeDetailList.observe(this) { themeDetailList ->
            themeDetailList?.let {
                themeDetailAdapter.updateData(it)

                // 更新标题（从第一个TitleItem获取）
                val titleItem = it.firstOrNull { item -> item is TitleItem } as? TitleItem
                titleItem?.let { title ->
                    textView.text = title.title
                }
            }
        }

        // 观察加载状态
        viewModel.loading.observe(this) { isLoading ->
            // 这里可以显示/隐藏加载进度条
            if (isLoading) {
                // 显示加载状态
                // progressBar.visibility = View.VISIBLE
            } else {
                // 隐藏加载状态
                // progressBar.visibility = View.GONE
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
        // 获取当前主题信息
        val currentData = viewModel.themeDetailList.value
        val titleItem = currentData?.firstOrNull { it is TitleItem } as? TitleItem

        if (titleItem != null) {
            val shareText = "分享主题: ${titleItem.title}\n${titleItem.subTitle}\n${titleItem.shareLink}"

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