package com.generals.module_rankings.ui.fragment

import RankingViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.module_rankings.R
import com.generals.module_rankings.model.bean.Item
import com.generals.module_rankings.model.bean.NavigationEvent
import com.generals.module_rankings.ui.adapter.SimpleVideoAdapter

import kotlinx.coroutines.launch
import com.alibaba.android.arouter.launcher.ARouter
import com.generals.module_rankings.model.bean.VideoType
import com.generals.module_rankings.ui.adapter.OnItemClickListener

class VideoFragment : Fragment() {

    // Views
    private lateinit var recyclerView: RecyclerView
    private var isFeaturedVideoRemoved = false // 标记是否已移除视频

    // ViewModel - 复用现有的RankingViewModel
    private val viewModel: RankingViewModel by viewModels()

    // ExoPlayer和适配器
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var adapter: SimpleVideoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_month, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupExoPlayer()
        setupRecyclerView()
        observeViewModel()
        loadData()
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
    }

    private fun setupExoPlayer() {
        exoPlayer = ExoPlayer.Builder(requireContext()).build().apply {
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    private fun setupRecyclerView() {
        adapter = SimpleVideoAdapter(exoPlayer, object : OnItemClickListener {
            override fun onItemClick(item: Any) {
                viewModel.onItemClick(item)
            }
        })

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@VideoFragment.adapter

            // 滚动监听
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    checkVideoVisibility()
                    checkIfVideoIsGoneAndRemove()
                }
            })
        }
    }

    private fun checkIfVideoIsGoneAndRemove() {
        if (isFeaturedVideoRemoved) return

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()

        // 如果第一个可见的item不是位置0，说明视频已经滑出
        if (firstVisiblePosition > 0) {
            Log.d("VideoFragment", "视频已滑出，第一个可见位置: $firstVisiblePosition")
            viewModel.demoteFeaturedVideoToNormalItem()
            isFeaturedVideoRemoved = true
            return
        }

        // 或者检测位置0的view是否还在屏幕内
        val videoView = layoutManager.findViewByPosition(0)
        if (videoView != null && videoView.bottom <= 0) {
            Log.d("VideoFragment", "视频view已滑出屏幕顶部")
            viewModel.demoteFeaturedVideoToNormalItem()
            isFeaturedVideoRemoved = true
        }
    }



    private fun observeViewModel() {
        // 观察视频列表数据
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.videoList.collect { videos ->
                adapter.updateList(videos)
                // 只有当前是 VideoType 才 setup 播放器
                if (videos.firstOrNull() is VideoType) {
                    setupVideoPlayer()
                }
            }

        }

        // 观察视频模式状态
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isVideoMode.collect { isVideoMode ->
                exoPlayer.let { player ->
                    if (isVideoMode && isResumed) {
                        player.play()
                    } else {
                        player.pause()
                        // 如果视频已被移除，完全停止播放器
                        if (viewModel.getFeaturedVideo() == null) {
                            Log.d("VideoFragment", "视频已移除，停止播放器")
                            player.stop()
                            player.clearMediaItems()
                        }
                    }
                }
            }
        }

        // 观察导航事件
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigationEvent.collect { event ->
                event?.let {
                    when (it) {
                        is NavigationEvent.ToVideoDetail -> {
                            navigateToVideoDetail(it.item)
                        }
                    }
                    viewModel.clearNavigationEvent()
                }
            }
        }
    }

    private fun setupVideoPlayer() {
        val firstVideo = viewModel.getFeaturedVideo()
        firstVideo?.let { item ->
            val mediaItem = MediaItem.fromUri(item.data.playUrl)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
        }
    }

    private fun checkVideoVisibility() {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstPosition = layoutManager.findFirstVisibleItemPosition()
        val firstView = layoutManager.findViewByPosition(0)

        val isVideoVisible = firstPosition == 0 &&
                firstView != null &&
                firstView.bottom > firstView.height * 0.7

        viewModel.updateVideoVisibility(isVideoVisible)
    }

    private fun loadData() {
        val strategy = arguments?.getString("strategy") ?: "monthly"
        viewModel.loadVideoList(strategy)
    }

    private fun navigateToVideoDetail(item: Item) {
        // 复用你现有的跳转逻辑
        try {
            val id = item.data.id
            val title = item.data.title
            val authorName = item.data.author.name
            val authorIcon = item.data.author.icon
            val authorDescription = item.data.author.description
            val subTitle = (item.data.author.name ) + " / #" + (item.data.category )
            val description = item.data.description
            val collectionCount = item.data.consumption.collectionCount
            val shareCount = item.data.consumption.shareCount
            val replyCount = item.data.consumption.replyCount
            val background = item.data.cover.blurred
            val cover = item.data.cover.detail
            val playUrl = item.data.playUrl
            val likeCount = item.data.consumption.collectionCount

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
            Log.e("VideoFragment", "跳转失败", e)
        }
    }


    // Fragment生命周期
    override fun onResume() {
        super.onResume()
        if (::exoPlayer.isInitialized && viewModel.isVideoMode.value) {
            exoPlayer.play()
        }
    }

    override fun onPause() {
        super.onPause()
        if (::exoPlayer.isInitialized) {
            exoPlayer.pause()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::exoPlayer.isInitialized) {
            exoPlayer.release()
        }
    }
}