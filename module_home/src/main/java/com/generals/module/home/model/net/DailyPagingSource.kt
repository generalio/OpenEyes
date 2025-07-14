package com.generals.module.home.model.net

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.generals.module.home.model.bean.Daily
import kotlin.random.Random

/**
 * @Desc : 日报的Paging3
 * @Author : zzx
 * @Date : 2025/7/14 10:06
 */

class DailyPagingSource(private val dailyService: DailyService) : PagingSource<Int, Daily>() {
    override fun getRefreshKey(state: PagingState<Int, Daily>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Daily> {
        return try {
            val page = params.key ?: 1
            val dailyResponse = dailyService.getDaily(page)
            val dailyList = dailyResponse.itemList
            val preKey = if(page > 1) page - 1 else null
            val nextKey = if(dailyList.isNotEmpty()) page + 1 else null
            LoadResult.Page(dailyList, preKey, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}