package com.generals.module.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.generals.module.home.model.bean.daily.Daily
import com.generals.module.home.model.net.HomeRepository
import kotlinx.coroutines.flow.Flow

/**
 * @Desc : 日报的ViewModel
 * @Author : zzx
 * @Date : 2025/7/14 10:38
 */

class DailyViewModel : ViewModel() {

    fun getDaily() : Flow<PagingData<Daily>> {
        return HomeRepository.getDaily().flow.cachedIn(viewModelScope)
    }

}