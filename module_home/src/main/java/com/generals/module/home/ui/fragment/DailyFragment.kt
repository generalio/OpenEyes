package com.generals.module.home.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.generals.module.home.R
import com.generals.module.home.model.bean.daily.Daily
import com.generals.module.home.ui.activity.HomeActivity
import com.generals.module.home.ui.adapter.DailyAdapter
import com.generals.module.home.ui.adapter.FooterAdapter
import com.generals.module.home.viewmodel.DailyViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DailyFragment : Fragment(), DailyAdapter.OnItemClickListener {

    private val viewModel: DailyViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var homeActivity: HomeActivity
    private lateinit var loadingLayout: View
    private lateinit var progressLoading: ProgressBar
    private lateinit var mBtnRetry: Button
    private lateinit var mTvLoading: TextView

    private lateinit var adapter: DailyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DailyAdapter(this)
        homeActivity = activity as HomeActivity

        loadingLayout = view.findViewById(R.id.daily_layout_load)
        progressLoading = loadingLayout.findViewById(R.id.progress_load)
        mBtnRetry = loadingLayout.findViewById(R.id.btn_retry)
        mTvLoading = loadingLayout.findViewById(R.id.tv_load)

        recyclerView = view.findViewById(R.id.rv_daily)
        recyclerView.layoutManager = LinearLayoutManager(homeActivity)
        recyclerView.adapter =
            adapter.withLoadStateFooter(FooterAdapter { adapter.retry() }) // 设置adapter以及断网的adapter
        recyclerView.visibility = View.GONE
        mTvLoading.text = "正在加载中..."
        showLoading()
        initEvent()

    }

    private fun initEvent() {
        checkNetWork()
        mBtnRetry.setOnClickListener {
            showLoading()
            checkNetWork()
        }
    }

    // 检查网络是否链接
    private fun checkNetWork() {
        if (homeActivity.isNetworkAvailable()) {
            loadData()
        } else {
            mTvLoading.visibility = View.GONE
            progressLoading.visibility = View.GONE
            mBtnRetry.visibility = View.VISIBLE
            homeActivity.showToast("网络链接失败，请重试!")
        }
    }

    // 加载数据
    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getDaily().collectLatest {
                    recyclerView.visibility = View.VISIBLE
                    adapter.submitData(it)
                }
            }
        }
        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.NotLoading) {
                hideLoading()
            }
        }
    }

    // 显示加载
    private fun showLoading() {
        loadingLayout.visibility = View.VISIBLE
        progressLoading.visibility = View.VISIBLE
        mTvLoading.visibility = View.VISIBLE
        mBtnRetry.visibility = View.GONE
    }

    // 隐藏加载
    private fun hideLoading() {
        loadingLayout.visibility = View.GONE
        progressLoading.visibility = View.GONE
        mTvLoading.visibility = View.GONE
    }

    // 普通Video跳转
    override fun onVideoClick(daily: Daily) {
        val id = daily.data.content.data.id
        val title = daily.data.content.data.title
        val authorName = daily.data.content.data.author.name
        val authorIcon = daily.data.content.data.author.icon
        val authorDescription = daily.data.content.data.author.description
        val subTitle =
            daily.data.content.data.author.name + " / #" + daily.data.content.data.category
        val description = daily.data.content.data.description
        val collectionCount = daily.data.content.data.consumption.realCollectionCount
        val shareCount = daily.data.content.data.consumption.shareCount
        val replyCount = daily.data.content.data.consumption.replyCount
        val background = daily.data.content.data.cover.blurred
        val cover = daily.data.content.data.cover.detail
        val playUrl = daily.data.content.data.playUrl
        val likeCount = daily.data.content.data.consumption.collectionCount

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

}