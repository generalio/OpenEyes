package com.generals.module.video.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.generals.module.video.R

/**
 * @Desc : Paging3底部刷新Adapter
 * @Author : zzx
 * @Date : 2025/7/16 17:22
 */

class FooterLoadingAdapter(val retry: () -> Unit) :
    LoadStateAdapter<FooterLoadingAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val progressBar: ProgressBar = view.findViewById(R.id.progress_load)
        val mTvLoad: TextView = view.findViewById(R.id.tv_load)
        val mBtnRetry: Button = view.findViewById(R.id.btn_retry)

        init {
            mBtnRetry.setOnClickListener {
                retry()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.progressBar.isVisible = loadState is LoadState.Loading
        holder.mTvLoad.isVisible = loadState is LoadState.Loading
        holder.mBtnRetry.isVisible = loadState is LoadState.Error
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_loading, parent, false)
        return ViewHolder(view)
    }

}