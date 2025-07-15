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

    /**
     * 原因在这里，之前是在方法里面每次都new一个flow，所以每次拿到的flow都是新的，数据就会全量刷新，很影响体验
     * 现在采用viewmodel保存下flow，每次调用方法返回
     */

    private val dailyFlow = HomeRepository.getDaily().flow.cachedIn(viewModelScope)

    fun getDaily() : Flow<PagingData<Daily>> {
        return dailyFlow
    }

}