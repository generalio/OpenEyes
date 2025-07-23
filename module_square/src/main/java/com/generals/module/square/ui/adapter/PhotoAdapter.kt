package com.generals.module.square.ui.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.generals.module.square.R

/**
 * @Desc : 类的描述
 * @Author : zzx
 * @Date : 2025/7/20 20:53
 */

class PhotoAdapter(private val photoList: List<String>, private val getHeight : GetDetail) : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    interface GetDetail {
        fun getHeight(fl: Float)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mIvPhoto: ImageView = view.findViewById(R.id.iv_photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.mIvPhoto.context)
            .load(photoList[position])
            .error(R.drawable.ic_failed)
            .apply(RequestOptions().centerCrop())
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    holder.mIvPhoto.setImageDrawable(resource)
                    getHeight.getHeight(holder.mIvPhoto.height.toFloat())
                }

                override fun onLoadCleared(placeholder: Drawable?) {}

            })
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

}