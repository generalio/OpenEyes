package com.generals.module.video.model.net

import com.generals.module.video.model.bean.CommentResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @Desc : 最新评论的Service
 * @Author : zzx
 * @Date : 2025/7/16 16:46
 */

interface NewCommentService {

    @GET("v2/replies/video")
    suspend fun getNewComment(@Query("lastId") lastId: String?, @Query("videoId") videoId: Int,@Query("num") num: String?) : CommentResponse
}