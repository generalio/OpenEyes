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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.generals.module.home.R
import com.generals.module.home.ui.activity.HomeActivity
import com.generals.module.home.ui.adapter.DailyAdapter
import com.generals.module.home.ui.adapter.FooterAdapter
import com.generals.module.home.viewmodel.DailyViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DailyFragment : Fragment() {

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

        adapter = DailyAdapter()
        homeActivity = activity as HomeActivity

        loadingLayout = view.findViewById(R.id.layout_load)
        progressLoading = loadingLayout.findViewById(R.id.progress_load)
        mBtnRetry = loadingLayout.findViewById(R.id.btn_retry)
        mTvLoading = loadingLayout.findViewById(R.id.tv_load)

        recyclerView = view.findViewById(R.id.rv_daily)
        recyclerView.layoutManager = LinearLayoutManager(homeActivity)
        recyclerView.adapter = adapter.withLoadStateFooter(FooterAdapter{ adapter.retry() })
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

    private fun checkNetWork() {
        if(homeActivity.isNetworkAvailable()) {
            loadData()
        } else {
            mTvLoading.visibility = View.GONE
            progressLoading.visibility = View.GONE
            mBtnRetry.visibility = View.VISIBLE
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getDaily().collectLatest {
                    recyclerView.visibility = View.VISIBLE
                    hideLoading()
                    adapter.submitData(it)
                }
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

}