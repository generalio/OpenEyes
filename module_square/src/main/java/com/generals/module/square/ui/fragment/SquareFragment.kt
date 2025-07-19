package com.generals.module.square.ui.fragment

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.generals.lib.base.BaseActivity
import com.generals.module.square.R
import com.generals.module.square.model.bean.ItemDetail
import com.generals.module.square.model.bean.Square
import com.generals.module.square.ui.adapter.BannerContentAdapter
import com.generals.module.square.ui.adapter.BannerItemAdapter
import com.generals.module.square.ui.adapter.SquareAdapter
import com.generals.module.square.viewmodel.SquareViewModel

class SquareFragment : Fragment() {

    private val squareViewModel : SquareViewModel by viewModels()

    private lateinit var baseActivity: BaseActivity
    private lateinit var recyclerview: RecyclerView
    private lateinit var bannerAdapter: BannerItemAdapter
    private lateinit var squareAdapter: SquareAdapter
    private lateinit var loading: ImageView

    private var rotateAnimator: ObjectAnimator? = null

    private var startScore = "0"
    private val squareList: MutableList<Square> = mutableListOf()
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_square, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        baseActivity = activity as BaseActivity

        recyclerview = view.findViewById(R.id.rv_square)
        loading = view.findViewById(R.id.iv_loading)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerview.layoutManager = layoutManager

        bannerAdapter = BannerItemAdapter(listOf())
        squareAdapter = SquareAdapter()
        recyclerview.adapter = ConcatAdapter(bannerAdapter, squareAdapter)
        checkNetWork()
        listenLiveData()
        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 如果不能继续向下滑（已经滑倒底部）
                if(!isLoading && !recyclerView.canScrollVertically(1)) {
                    loadMoreData()
                }
            }
        })
        loading.setOnClickListener {
            startAnimate()
        }
    }

    private fun loadMoreData() {
        isLoading = true
        startAnimate()
    }

    private fun checkNetWork() {
        if(baseActivity.isNetworkAvailable()) {
            loadData()
        } else {
            showToast("网络链接失败，请重试!")
            loading.animate().translationY(-loading.height.toFloat())
                .setInterpolator(DecelerateInterpolator())
            recyclerview.animate().translationY(-loading.height.toFloat()).setInterpolator(
                DecelerateInterpolator()
            )
        }
    }

    private fun loadData() {
        squareViewModel.getSquareBannerInfo()
        squareViewModel.getSquareInfo(startScore, 2)
    }

    fun showToast(content: String) {
        Toast.makeText(baseActivity, content, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listenLiveData() {
        squareViewModel.bannerLiveData.observe(viewLifecycleOwner) {
            val bannerList: MutableList<ItemDetail> = mutableListOf()
            bannerList.add(it[it.size - 1])
            bannerList.addAll(it)
            bannerList.add(it[0])
            bannerAdapter = BannerItemAdapter(bannerList)
            bannerAdapter.notifyDataSetChanged()
            recyclerview.adapter = ConcatAdapter(bannerAdapter, squareAdapter)
        }
        squareViewModel.squareLiveData.observe(viewLifecycleOwner) { response ->
            startScore = Uri.parse(response.nextPageUrl).getQueryParameter("startScore") ?: "0"
            for(item in response.itemList) {
                if(item.data.dataType == "FollowCard") {
                    if(item.data.content != null) {
                        if(item.data.content.type != null) {
                            if(item.data.content.type == "ugcPicture") {
                                squareList.add(item)
                            }
                        }
                    }
                }
            }
            isLoading = false
            squareAdapter.submitList(squareList.toList())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bannerAdapter.release()
        rotateAnimator?.cancel()
        rotateAnimator = null
    }

    fun startAnimate() {
        // 回弹到一定高度进行旋转
        loading.animate().translationY(-loading.height.toFloat())
            .setInterpolator(DecelerateInterpolator())
        recyclerview.animate().translationY(-loading.height.toFloat()).setInterpolator(
            DecelerateInterpolator()
        )
        if (rotateAnimator?.isRunning == true) return //防止重复创建
        rotateAnimator =
            ObjectAnimator.ofFloat(loading, "rotation", loading.rotation, loading.rotation - 360F)
                .apply {
                    duration = 1000
                    repeatCount = 0
                    interpolator = LinearInterpolator()
                    addListener(object : AnimatorListener {
                        override fun onAnimationStart(p0: Animator) {
                            // 开始刷新
                            loading.isClickable = false
                            if(baseActivity.isNetworkAvailable()) {
                                squareViewModel.getSquareInfo(startScore, 2)
                            } else {
                                showToast("网络链接失败，请重试!")
                                isLoading = false
                            }
                        }

                        override fun onAnimationEnd(p0: Animator) {
                            // 结束刷新
                            loading.isClickable = true
                            if(baseActivity.isNetworkAvailable()) {
                                stopAnimate()
                            }
                        }

                        override fun onAnimationCancel(p0: Animator) {}

                        override fun onAnimationRepeat(p0: Animator) {}

                    })
                    start()
                }
    }

    fun stopAnimate() {
        loading.animate().translationY(0F).setInterpolator(DecelerateInterpolator())
        recyclerview.animate().translationY(0F).setInterpolator(DecelerateInterpolator())
    }

}