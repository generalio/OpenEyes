package com.generals.module.video.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.generals.module.video.R
import com.generals.module.video.model.bean.related.VideoRelated
import com.generals.module.video.ui.activity.VideoActivity
import com.generals.module.video.ui.adapter.DetailInfoAdapter
import com.generals.module.video.ui.adapter.DetailRelatedAdapter
import com.generals.module.video.ui.adapter.FooterAdapter
import com.generals.module.video.ui.custom.LoadingLayout
import com.generals.module.video.viewmodel.DetailViewModel

class VideoDetailFragment : Fragment(), DetailInfoAdapter.OnItemClickListener, DetailRelatedAdapter.OnItemClickListener {

    private lateinit var videoActivity: VideoActivity
    private val viewModel : DetailViewModel by viewModels()

    private lateinit var loadingLayout: LoadingLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var relatedAdapter: DetailRelatedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoActivity = activity as VideoActivity
        loadingLayout = view.findViewById(R.id.layout_detail_loading)
        recyclerView = view.findViewById(R.id.rv_video_detail)
        recyclerView.layoutManager = LinearLayoutManager(videoActivity)
        relatedAdapter = DetailRelatedAdapter(this)

        initEvent()
        listenLiveData()
    }

    private fun initEvent() {
        checkNetWork()
        loadingLayout.onRotateStop = {
            checkNetWork()
        }
    }

    private fun checkNetWork() {
        if(videoActivity.isNetworkAvailable()) {
            loadingLayout.stopAnimate()
            loadData()
        } else {
            videoActivity.showToast("网络链接失败，请重试!")
        }
    }

    private fun listenLiveData() {
        viewModel.videoRelatedLiveData.observe(viewLifecycleOwner) {
            relatedAdapter.submitList(it.toList())
            recyclerView.adapter = ConcatAdapter(DetailInfoAdapter(videoActivity.videoInfo, this), relatedAdapter ,FooterAdapter())
        }
    }

    private fun loadData() {
        viewModel.getVideoRelated(videoActivity.videoInfo.id)
    }

    override fun onZanClick() {
        videoActivity.showToast("请登录后再点赞!")
    }

    override fun onCollectClick() {
        videoActivity.showToast("请登录后再收藏!")
    }

    override fun onCommentClick() {
        videoActivity.viewPager2.currentItem = 1
    }

    override fun onShareClick() {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, "【${videoActivity.videoInfo.title}-${videoActivity.videoInfo.authorName}】${videoActivity.videoInfo.playUrl}")
        intent.type = "text/plain"
        startActivity(Intent.createChooser(intent, "分享到"))
    }

    override fun onDescriptionClick() {
        videoActivity.copyText(videoActivity.videoInfo.description)
    }

    override fun onVideoCoverClick(videoInfo: VideoRelated) {
        val id = videoInfo.data.id
        val title = videoInfo.data.title
        val authorName = videoInfo.data.author.name
        val authorIcon = videoInfo.data.author.icon
        val authorDescription = videoInfo.data.author.description
        val subTitle = videoInfo.data.author.name + " / #" + videoInfo.data.category
        val description = videoInfo.data.description
        val collectionCount = videoInfo.data.consumption.realCollectionCount
        val shareCount = videoInfo.data.consumption.shareCount
        val replyCount = videoInfo.data.consumption.replyCount
        val background = videoInfo.data.cover.blurred
        val cover = videoInfo.data.cover.detail
        val playUrl = videoInfo.data.playUrl
        val likeCount = videoInfo.data.consumption.collectionCount

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