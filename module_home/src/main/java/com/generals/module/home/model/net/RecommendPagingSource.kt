package com.generals.module.home.model.net

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.generals.module.home.model.bean.recommend.Recommend

/**
 * @Desc : 推荐页Paging3的Source
 * @Author : zzx
 * @Date : 2025/7/14 20:21
 */

class RecommendPagingSource(private val recommendService: RecommendService) : PagingSource<Int, Recommend>() {
    override fun getRefreshKey(state: PagingState<Int, Recommend>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Recommend> {
        return try {
            val page = params.key ?: 1
            val recommendResponse = recommendService.getRecommend(page)
            val recommendItems = recommendResponse.itemList
            val nextKey = if(recommendItems.isNotEmpty()) page + 1 else null
            LoadResult .Page(recommendItems, null, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}