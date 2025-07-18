package com.generals.module.square.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.generals.lib.base.BaseActivity
import com.generals.module.square.R
import com.generals.module.square.model.bean.ItemDetail
import com.generals.module.square.ui.adapter.BannerContentAdapter
import com.generals.module.square.ui.adapter.BannerItemAdapter
import com.generals.module.square.viewmodel.SquareViewModel

class SquareFragment : Fragment() {

    private val squareViewModel : SquareViewModel by viewModels()

    private lateinit var baseActivity: BaseActivity
    private lateinit var recyclerview: RecyclerView
    private lateinit var bannerAdapter: BannerItemAdapter

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
        recyclerview.layoutManager = LinearLayoutManager(baseActivity)
        bannerAdapter = BannerItemAdapter(listOf())
        checkNetWork()
        listenLiveData()
    }

    private fun checkNetWork() {
        if(baseActivity.isNetworkAvailable()) {
            loadData()
        } else {
            showToast("网络链接失败，请重试!")
        }
    }

    private fun loadData() {
        squareViewModel.getSquareBannerInfo()
    }

    fun showToast(content: String) {
        Toast.makeText(baseActivity, content, Toast.LENGTH_SHORT).show()
    }

    private fun listenLiveData() {
        squareViewModel.bannerLiveData.observe(viewLifecycleOwner) {
            val bannerList: MutableList<ItemDetail> = mutableListOf()
            bannerList.add(it[it.size - 1])
            bannerList.addAll(it)
            bannerList.add(it[0])
            bannerAdapter = BannerItemAdapter(bannerList)
            recyclerview.adapter = bannerAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bannerAdapter.release()
    }
}