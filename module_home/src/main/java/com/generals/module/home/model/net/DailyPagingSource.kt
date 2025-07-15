package com.generals.module.home.model.net

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.generals.module.home.model.bean.daily.Daily

/**
 * @Desc : 日报的Paging3
 * @Author : zzx
 * @Date : 2025/7/14 10:06
 */

class DailyPagingSource(private val dailyService: DailyService) : PagingSource<String, Daily>() {

    override fun getRefreshKey(state: PagingState<String, Daily>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Daily> {
        /**
         * 这里接口不是以页数给出的而是有个nextPageUrl字段
         * 故提取出其中的date="..."的内容作为下一次请求的标识
         * preKey如果不上拉刷新无影响
         */
        return try {
            val pageUrl = params.key
            val date = pageUrl?.let {
                Uri.parse(it).getQueryParameter("date")
            }
            val dailyResponse = dailyService.getDaily(date)
            val dailyList = dailyResponse.itemList
            val nextPageUrl = dailyResponse.nextPageUrl
            LoadResult.Page(dailyList,null,nextPageUrl)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


}