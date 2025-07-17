package com.generals.module.video.model.net

import com.generals.module.video.model.bean.related.VideoRelatedResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @Desc : 推荐视频的接口
 * @Author : zzx
 * @Date : 2025/7/17 14:20
 */

interface VideoRelatedService {

    @GET("v4/video/related")
    fun getVideoRelated(@Query("id")videoId: Int) : Observable<VideoRelatedResponse>

}