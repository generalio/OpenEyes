package com.generals.module.video.model.repository

import com.generals.lib.net.ServiceCreator
import com.generals.module.video.model.net.VideoRelatedService

/**
 * @Desc : 详情页的仓库层
 * @Author : zzx
 * @Date : 2025/7/17 14:27
 */

object DetailRepository {

    private val videoRelatedService = ServiceCreator.create<VideoRelatedService>()

    fun getVideoRelated(videoId: Int) = videoRelatedService.getVideoRelated(videoId)

}