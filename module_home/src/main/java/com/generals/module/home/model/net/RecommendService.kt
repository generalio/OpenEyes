package com.generals.module.home.model.net

import com.generals.module.home.model.bean.daily.DailyResponse
import com.generals.module.home.model.bean.recommend.RecommendResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @Desc : 类的描述
 * @Author : zzx
 * @Date : 2025/7/14 20:21
 */

interface RecommendService {

    @GET("v5/index/tab/allRec")
    suspend fun getRecommend(@Query("page") page: Int) : RecommendResponse

}