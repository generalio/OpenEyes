package com.generals.module.video.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.generals.module.video.R
import com.generals.module.video.ui.activity.VideoActivity
import com.generals.module.video.ui.adapter.DetailInfoAdapter
import com.generals.module.video.ui.adapter.FooterAdapter
import com.generals.module.video.ui.custom.LoadingLayout

class VideoDetailFragment : Fragment(), DetailInfoAdapter.OnItemClickListener {

    private lateinit var videoActivity: VideoActivity

    private lateinit var loadingLayout: LoadingLayout
    private lateinit var recyclerView: RecyclerView

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

        initEvent()
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

    private fun loadData() {
        val adapter = ConcatAdapter(DetailInfoAdapter(videoActivity.videoInfo, this), FooterAdapter())
        recyclerView.adapter = adapter
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
}