package com.generals.module.square.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.generals.lib.base.Util
import com.generals.module.square.R
import com.generals.module.square.model.bean.ItemDetail

/**
 * @Desc : 轮播图的Banner
 * @Author : zzx
 * @Date : 2025/7/18 14:25
 */

class BannerContentAdapter(private val bannerList: List<ItemDetail>) :
    RecyclerView.Adapter<BannerContentAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mIvContent: ImageView = view.findViewById(R.id.iv_banner_content)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = bannerList[position % bannerList.size]
        Glide.with(holder.mIvContent.context)
            .load(Util.convertImgUrl(item.data.image))
            .error(R.drawable.ic_failed)
            .transform(RoundedCorners(40))
            .into(holder.mIvContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_banner_content, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bannerList.size
    }

}