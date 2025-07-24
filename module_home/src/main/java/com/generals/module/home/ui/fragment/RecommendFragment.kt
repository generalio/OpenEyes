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
import com.generals.module.home.model.bean.recommend.Recommend
import com.generals.module.home.ui.activity.HomeActivity
import com.generals.module.home.ui.adapter.FooterAdapter
import com.generals.module.home.ui.adapter.RecommendAdapter
import com.generals.module.home.viewmodel.RecommendViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RecommendFragment : Fragment(), RecommendAdapter.OnItemClickListener {

    private val viewModel: RecommendViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var homeActivity: HomeActivity
    private lateinit var loadingLayout: View
    private lateinit var progressLoading: ProgressBar
    private lateinit var mBtnRetry: Button
    private lateinit var mTvLoading: TextView

    private lateinit var adapter: RecommendAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recommend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecommendAdapter(this)
        homeActivity = activity as HomeActivity

        loadingLayout = view.findViewById(R.id.recommend_layout_load)
        progressLoading = loadingLayout.findViewById(R.id.progress_load)
        mBtnRetry = loadingLayout.findViewById(R.id.btn_retry)
        mTvLoading = loadingLayout.findViewById(R.id.tv_load)

        recyclerView = view.findViewById(R.id.rv_recommend)
        recyclerView.layoutManager = LinearLayoutManager(homeActivity)
        recyclerView.adapter = adapter.withLoadStateFooter(FooterAdapter { adapter.retry() })
        recyclerView.visibility = View.GONE
        mTvLoading.text = "正在加载中..."

        showLoading()
        initEvent()

    }

    private fun initEvent() {
        checkNetWork()
        // 重新拉取网络请求
        mBtnRetry.setOnClickListener {
            showLoading()
            checkNetWork()
        }
    }

    // 检查网络链接
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

    private fun loadData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getRecommend().collectLatest {
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

    private fun showLoading() {
        loadingLayout.visibility = View.VISIBLE
        progressLoading.visibility = View.VISIBLE
        mTvLoading.visibility = View.VISIBLE
        mBtnRetry.visibility = View.GONE
    }

    private fun hideLoading() {
        loadingLayout.visibility = View.GONE
        progressLoading.visibility = View.GONE
        mTvLoading.visibility = View.GONE
    }

    override fun onVideoClick(recommend: Recommend) {
        val id = recommend.data.content.data.id
        val title = recommend.data.content.data.title
        val authorName = recommend.data.content.data.author.name
        val authorIcon = recommend.data.content.data.author.icon
        val authorDescription = recommend.data.content.data.author.description
        val subTitle =
            recommend.data.content.data.author.name + " / #" + recommend.data.content.data.category
        val description = recommend.data.content.data.description
        val collectionCount = recommend.data.content.data.consumption.realCollectionCount
        val shareCount = recommend.data.content.data.consumption.shareCount
        val replyCount = recommend.data.content.data.consumption.replyCount
        val background = recommend.data.content.data.cover.blurred
        val cover = recommend.data.content.data.cover.detail
        val playUrl = recommend.data.content.data.playUrl
        val likeCount = recommend.data.content.data.consumption.collectionCount
        goToVideo(
            id,
            title,
            subTitle,
            description,
            collectionCount,
            shareCount,
            replyCount,
            background,
            cover,
            playUrl,
            authorName,
            authorIcon,
            authorDescription,
            likeCount
        )
    }

    // 小型视频的跳转（接口数据与普通的不一样）
    override fun onVideoSmallClick(recommend: Recommend) {
        val id = recommend.data.id
        val title = recommend.data.title
        val authorName = recommend.data.author.name
        val authorIcon = recommend.data.author.icon
        val authorDescription = recommend.data.author.description
        val subTitle = recommend.data.author.name + " / #" + recommend.data.category
        val description = recommend.data.description
        val collectionCount = recommend.data.consumption.realCollectionCount
        val shareCount = recommend.data.consumption.shareCount
        val replyCount = recommend.data.consumption.replyCount
        val background = recommend.data.cover.blurred
        val cover = recommend.data.cover.detail
        val playUrl = recommend.data.playUrl
        val likeCount = recommend.data.consumption.collectionCount
        goToVideo(
            id,
            title,
            subTitle,
            description,
            collectionCount,
            shareCount,
            replyCount,
            background,
            cover,
            playUrl,
            authorName,
            authorIcon,
            authorDescription,
            likeCount
        )
    }

    private fun goToVideo(
        id: Int,
        title: String,
        subTitle: String,
        description: String,
        collectionCount: Int,
        shareCount: Int,
        replyCount: Int,
        background: String,
        cover: String,
        playUrl: String,
        authorName: String,
        authorIcon: String,
        authorDescription: String,
        likeCount: Int
    ) {
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