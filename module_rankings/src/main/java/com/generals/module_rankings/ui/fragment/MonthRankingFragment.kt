package com.generals.module_rankings.ui.fragment

import RankingViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.example.module_rankings.R
import com.generals.module_rankings.model.bean.Item
import com.generals.module_rankings.ui.adapter.RankingAdapter
import com.generals.module_rankings.ui.adapter.RankingClickListener
import kotlinx.coroutines.launch

class MonthRankingFragment : Fragment() {
    private lateinit var viewModel: RankingViewModel
    private lateinit var adapter: RankingAdapter

    companion object {
        fun newInstance(): MonthRankingFragment {
            return MonthRankingFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 在 onCreate 中初始化 ViewModel
        viewModel = ViewModelProvider(requireParentFragment())[RankingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_month, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        setupRecyclerView(recyclerView)
        observeData()
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        adapter = RankingAdapter(mutableListOf(),object :RankingClickListener{
            override fun onVideoClick(position: Int, item: Item) {
                playVideo(item)
            }

            override fun onButtonClick(position: Int, item: Item) {
                TODO("Not yet implemented")
            }

        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.isNestedScrollingEnabled = true
    }

    private fun playVideo(item: Item) {
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

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.monthlyRanking.collect { data ->
                adapter.updateData(data)
            }
        }
    }
}