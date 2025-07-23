package com.generals.module.square.model.net

import com.generals.module.square.model.bean.SquareResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @Desc : 广场的接口
 * @Author : zzx
 * @Date : 2025/7/17 21:22
 */

interface SquareService {

    @GET("v7/community/tab/rec")
    fun getSquareInfo(@Query("startScore") startScore: String, @Query("pageCount") pageCount: Int) : Observable<SquareResponse>

    @GET("v7/community/tab/rec")
    fun getSquareBannerInfo() : Observable<SquareResponse>

}