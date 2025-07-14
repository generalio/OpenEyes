package com.generals.module.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.generals.lib.net.ServiceCreator
import com.generals.module.home.model.bean.Daily
import com.generals.module.home.model.net.DailyPagingSource
import com.generals.module.home.model.net.DailyService
import com.generals.module.home.model.net.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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