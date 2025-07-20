package com.generals.module.square.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.generals.module.square.R
import com.generals.module.square.model.bean.Photo
import com.generals.module.square.ui.activity.SquareDetailActivity

/**
 * @Desc : 详情的Adapter
 * @Author : zzx
 * @Date : 2025/7/20 20:47
 */

class SquareDetailAdapter(private val photoList: List<Photo>) : RecyclerView.Adapter<SquareDetailAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photoViewPager2: ViewPager2 = view.findViewById(R.id.vp2_square_photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_square_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.photoViewPager2.adapter = PhotoAdapter(photoList[position].urls)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

}