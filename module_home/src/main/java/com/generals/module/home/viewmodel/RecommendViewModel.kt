package com.generals.module.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.generals.module.home.model.net.HomeRepository

/**
 * @Desc : 推荐页的ViewModel
 * @Author : zzx
 * @Date : 2025/7/14 20:31
 */

class RecommendViewModel : ViewModel() {

    fun getRecommend() = HomeRepository.getRecommend().flow.cachedIn(viewModelScope)

}