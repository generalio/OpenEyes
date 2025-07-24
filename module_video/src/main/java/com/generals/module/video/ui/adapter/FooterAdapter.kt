package com.generals.module.video.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.generals.module.video.R

/**
 * @Desc : 底部的Adapter
 * @Author : zzx
 * @Date : 2025/7/16 16:16
 */

class FooterAdapter : RecyclerView.Adapter<FooterAdapter.FooterViewHolder>() {

    inner class FooterViewHolder(view: View) : ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FooterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_footer, parent, false)
        return FooterViewHolder(view)
    }

    override fun onBindViewHolder(holder: FooterViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 1
    }


}