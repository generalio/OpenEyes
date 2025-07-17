package com.example.module_discover.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.module_discover.model.bean.CategoryDetailItem
import com.example.module_discover.model.net.CategoryRepository
import kotlinx.coroutines.flow.Flow

class CategoryDetailViewModel : ViewModel() {

    // 在ViewModel内部创建Repository实例
    private val repository = CategoryRepository()

    fun getCategoryDetail(categoryId: Int): Flow<PagingData<CategoryDetailItem>> {
        return repository.getCategoryDetail(categoryId)
            .cachedIn(viewModelScope)
    }
}