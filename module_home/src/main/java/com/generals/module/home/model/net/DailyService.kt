package com.generals.module.home.model.net

import com.generals.module.home.model.bean.DailyResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @Desc : 日报的接口
 * @Author : zzx
 * @Date : 2025/7/14 09:58
 */

interface DailyService{

    @GET("v5/index/tab/feed?udid=435865baacfc49499632ea13c5a78f944c2f28aa")
    suspend fun getDaily(@Query("page") page: Int) : DailyResponse

}