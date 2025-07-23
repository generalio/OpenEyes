package com.generals.module_rankings.model.net

import com.generals.module_rankings.model.bean.RankingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RankingService {
    @GET("v4/rankList/videos")
    suspend fun getRanking(@Query("strategy")strategy:String):RankingResponse
}