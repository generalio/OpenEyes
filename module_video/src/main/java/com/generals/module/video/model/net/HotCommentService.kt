package com.generals.module.video.model.net

import com.generals.module.video.model.bean.CommentResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @Desc : 热门评论的Service
 * @Author : zzx
 * @Date : 2025/7/16 14:39
 */

interface HotCommentService {

    @GET("v2/replies/video")
    fun getHotCommentService(@Query("videoId") videoId: Int) : Observable<CommentResponse>
}