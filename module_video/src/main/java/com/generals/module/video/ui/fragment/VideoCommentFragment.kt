package com.generals.module.video.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.generals.module.video.R
import com.generals.module.video.model.bean.Comment
import com.generals.module.video.ui.activity.VideoActivity
import com.generals.module.video.ui.adapter.FooterAdapter
import com.generals.module.video.ui.adapter.FooterLoadingAdapter
import com.generals.module.video.ui.adapter.HotCommentAdapter
import com.generals.module.video.ui.adapter.NewCommentAdapter
import com.generals.module.video.ui.custom.LoadingLayout
import com.generals.module.video.viewmodel.CommentViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class VideoCommentFragment : Fragment() {

    private lateinit var videoActivity: VideoActivity
    private val viewModel: CommentViewModel by viewModels()

    private lateinit var mTvTitle: TextView
    private lateinit var mTvSort: TextView
    private lateinit var rvComment: RecyclerView
    private lateinit var loadingLayout: LoadingLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_comment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoActivity = activity as VideoActivity
        mTvTitle = view.findViewById(R.id.tv_comment_title)
        mTvSort = view.findViewById(R.id.tv_comment_sort)
        rvComment = view.findViewById(R.id.rv_comment)
        rvComment.layoutManager = LinearLayoutManager(videoActivity)
        loadingLayout = view.findViewById(R.id.layout_loading)

        initEvent()
        listenViewModel()
    }

    private fun initEvent() {
        checkNetWork()
        mTvSort.setOnClickListener {
            if(mTvSort.text == "按热度") {
                mTvSort.text = "按时间"
                mTvTitle.text = "最新评论"
                loadingLayout.startAnimate()
            } else {
                if(mTvSort.text == "按时间") {
                    mTvSort.text = "按热度"
                    mTvTitle.text = "最热评论"
                    loadingLayout.startAnimate()
                }
            }
        }
        loadingLayout.onRotateStart = {
            mTvSort.isClickable = false
        }
        loadingLayout.onRotateStop = {
            mTvSort.isClickable = true
            loadingLayout.stopAnimate()
            checkNetWork()
        }
    }

    private fun checkNetWork() {
        if(videoActivity.isNetworkAvailable()) {
            if(mTvSort.text == "按热度") {
                loadHotData()
            } else {
                if(mTvSort.text == "按时间") {
                    loadNewData()
                }
            }
        } else {
            videoActivity.showToast("网络链接失败，请重试!")
            mTvSort.text = if(mTvSort.text == "按时间") "按热度" else "按时间"
            mTvTitle.text = if(mTvTitle.text == "最热评论") "最新评论" else "最热评论"
        }
    }

    private fun loadHotData() {
        viewModel.getHotComment(videoActivity.videoInfo.id)
    }

    private fun loadNewData() {
        val adapter = NewCommentAdapter()
        var isFirst = false
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getNewComment(videoActivity.videoInfo.id).collectLatest {
                    rvComment.adapter = ConcatAdapter(adapter.withLoadStateFooter(FooterLoadingAdapter { adapter.retry() }), FooterAdapter())
                    adapter.submitData(it)
                }
            }
        }
        adapter.addLoadStateListener { loadState ->
            if(loadState.refresh is LoadState.NotLoading && adapter.itemCount > 0) {
                if(!isFirst) {
                    rvComment.scrollToPosition(0)
                    isFirst = true
                }
            }
        }
    }

    private fun listenViewModel() {
        viewModel.hotCommentLiveData.observe(viewLifecycleOwner) {
            val itemList = it.itemList
            val hotList: MutableList<Comment> = mutableListOf()
            for(item in itemList) {
                if(item.type == "reply") {
                    hotList.add(item)
                }
                if(item.type == "leftAlignTextHeader" && item.data.text == "最新评论") {
                    break
                }
            }
            // 特殊处理，有的视频接口无最热评论故用最新评论的第一页代替
            if(itemList[0].data.text == "最新评论") {
                for(item in itemList) {
                    if(item.type == "reply") {
                        hotList.add(item)
                    }
                }
            }
            val adapter = HotCommentAdapter()
            rvComment.adapter = ConcatAdapter(adapter, FooterAdapter())
            adapter.submitList(hotList.toList())
        }
    }
}