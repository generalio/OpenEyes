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

    val dailyService = ServiceCreator().create<DailyService>()

    fun getDaily() = Pager(config = PagingConfig(50), pagingSourceFactory = {
        DailyPagingSource(dailyService) }
    )

}