package com.generals.module.home.model.net

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.generals.lib.net.ServiceCreator

/**
 * @Desc : 主页仓库层
 * @Author : zzx
 * @Date : 2025/7/14 10:29
 */

object HomeRepository {

    private val dailyService = ServiceCreator.create<DailyService>()
    private val recommendService = ServiceCreator.create<RecommendService>()

    fun getDaily() = Pager(config = PagingConfig(50), pagingSourceFactory = {
        DailyPagingSource(dailyService) }
    )

    fun getRecommend() = Pager(config = PagingConfig(5), pagingSourceFactory = {RecommendPagingSource(
        recommendService)})

}