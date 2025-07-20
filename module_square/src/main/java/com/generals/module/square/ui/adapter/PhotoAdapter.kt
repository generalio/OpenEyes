package com.generals.module.square.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.generals.module.square.R
import com.generals.module.square.ui.activity.SquareDetailActivity

/**
 * @Desc : 类的描述
 * @Author : zzx
 * @Date : 2025/7/20 20:53
 */

class PhotoAdapter(private val photoList: List<String>) : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mIvPhoto: ImageView = view.findViewById(R.id.iv_photo)

        init {
            mIvPhoto.transitionName = "transition"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.mIvPhoto.context)
            .load(photoList[position])
            .error(R.drawable.ic_failed)
            .apply(RequestOptions().fitCenter())
            .into(holder.mIvPhoto)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

}