package com.generals.module.video.ui.activity

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.generals.lib.base.BaseActivity
import com.generals.module.video.R
import com.generals.module.video.model.bean.VideoInfo
import com.generals.module.video.ui.adapter.VP2Adapter
import com.generals.module.video.ui.fragment.VideoCommentFragment
import com.generals.module.video.ui.fragment.VideoDetailFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer

@Route(path = "/video/VideoActivity")
class VideoActivity : BaseActivity() {

    @Autowired
    @JvmField
    var id: Int = 0

    @Autowired
    @JvmField
    var title: String = ""

    @Autowired
    @JvmField
    var subTitle: String = ""

    @Autowired
    @JvmField
    var description: String = ""

    @Autowired
    @JvmField
    var collectionCount: Int = 0

    @Autowired
    @JvmField
    var shareCount: Int = 0

    @Autowired
    @JvmField
    var replyCount: Int = 0

    @Autowired
    @JvmField
    var background: String = ""

    @Autowired
    @JvmField
    var playUrl: String = ""

    @Autowired
    @JvmField
    var authorName: String = ""

    @Autowired
    @JvmField
    var authorIcon: String = ""

    @Autowired
    @JvmField
    var authorDescription: String = ""

    @Autowired
    @JvmField
    var cover: String = ""

    @Autowired
    @JvmField
    var likeCount: Int = 0

    private var isPlay = false
    private var isPause = false

    lateinit var videoInfo: VideoInfo
    lateinit var orientationUtils: OrientationUtils
    private lateinit var videoPlayer: StandardGSYVideoPlayer

    private lateinit var videoLayout: View
    private lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        ARouter.getInstance().inject(this)
        videoInfo = VideoInfo(id,title, subTitle, description, collectionCount, shareCount, replyCount, background, playUrl, authorName, authorIcon, authorDescription, cover, likeCount)

        initView()
        initEvent()
        initVideo()

    }

    private fun initView() {
        videoPlayer = findViewById(R.id.video_player)
        videoLayout = findViewById(R.id.layout_video)
        tabLayout = findViewById(R.id.tab_video)
        viewPager2 = findViewById(R.id.vp2_video)
    }

    private fun initEvent() {
        tabLayout.addTab(tabLayout.newTab().setText("简介"))
        tabLayout.addTab(tabLayout.newTab().setText("评论"))
        val fragmentList = listOf(
            { VideoDetailFragment() },
            { VideoCommentFragment() }
        )
        viewPager2.adapter = VP2Adapter(this,fragmentList)
        TabLayoutMediator(tabLayout,viewPager2) { tab, position ->
            if(position == 0) {
                tab.text = "简介"
            } else {
                if(position == 1) {
                    tab.text = "评论"
                }
            }
        }.attach()
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position) {
                    1 -> tab.text = "评 论"
                    0 -> tab.text = "简 介"
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                when(tab.position) {
                    1 -> tab.text = "评论"
                    0 -> tab.text = "简介"
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })
    }

    // 视频的一些初始化
    private fun initVideo() {
        orientationUtils = OrientationUtils(this, videoPlayer) // 外部辅助的旋转，帮助全屏
        orientationUtils.isEnable = false // 初始不打开外部的旋转
        val gsyVideoOption = GSYVideoOptionBuilder()
        val imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(this)
            .load(videoInfo.cover)
            .into(imageView)
        gsyVideoOption.setThumbImageView(imageView)
            .setIsTouchWiget(true)
            .setRotateViewAuto(false)
            .setLockLand(false)
            .setAutoFullWithSize(true)
            .setShowFullAnimation(false)
            .setNeedLockFull(true)
            .setUrl(videoInfo.playUrl)
            .setCacheWithPlay(false)
            .setVideoTitle(videoInfo.title)
            .setVideoAllCallBack(object : GSYSampleCallBack() {
                override fun onPrepared(url: String?, vararg objects: Any?) {
                    super.onPrepared(url, *objects)
                    orientationUtils.isEnable = true // 开始播放后才能全屏
                    isPlay = true
                }

                override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
                    super.onQuitFullscreen(url, *objects)
                    orientationUtils.backToProtVideo()
                }
            }).setLockClickListener { _, lock ->
                orientationUtils.isEnable = !lock
            }.build(videoPlayer)
        videoPlayer.fullscreenButton.setOnClickListener {
            orientationUtils.resolveByClick() // 直接横屏
            videoPlayer.startWindowFullscreen(this, true, true) // 是否隐藏actionBar和statusBar
        }
        videoPlayer.backButton.setOnClickListener {
            if(!GSYVideoManager.backFromWindowFull(this@VideoActivity)) {
                finish()
            } else {
                orientationUtils.backToProtVideo()
            }
        }

        // 配置生命周期
        onBackPressedDispatcher.addCallback(this) {
            orientationUtils.backToProtVideo()
            if(GSYVideoManager.backFromWindowFull(this@VideoActivity)) {
                return@addCallback
            }
            finish()
        }
    }

    override fun onPause() {
        videoPlayer.currentPlayer.onVideoPause()
        super.onPause()
        isPause = true
    }

    override fun onResume() {
        videoPlayer.currentPlayer.onVideoResume()
        super.onResume()
        isPause = false
    }

    override fun onDestroy() {
        super.onDestroy()
        if(isPlay) {
            videoPlayer.onVideoPause()
        }
        videoPlayer.currentPlayer.release()
        orientationUtils.releaseListener()
        GSYVideoManager.releaseAllVideos()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if(isPlay || !isPause) {
            videoPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true)
        }
    }

    fun showToast(content: String) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
    }

}