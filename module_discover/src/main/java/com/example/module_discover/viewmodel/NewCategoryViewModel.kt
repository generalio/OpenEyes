package com.example.module_discover.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.module_discover.model.bean.SpecialTopicsDetailResponse
import com.example.module_discover.model.net.CategoryRepository

class NewCategoryViewModel : ViewModel() {
    private val _dataList = MutableLiveData<List<SpecialTopicsDetailResponse>>()
    val dataList: LiveData<List<SpecialTopicsDetailResponse>> = _dataList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val repository = CategoryRepository()

    fun loadData(id: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                // 调用挂起函数获取 Result
                val result = repository.getSpecialTopicsDetail(id)

                result.onSuccess { response ->
                    // 将单个 SpecialTopicsDetailResponse 包装成 List
                    _dataList.value = listOf(response)
                }.onFailure { exception ->
                    _error.value = "加载失败: ${exception.message}"
                    _dataList.value = emptyList()
                }

            } catch (e: Exception) {
                _error.value = "请求异常: ${e.message}"
                _dataList.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshData(id: Int) {
        loadData(id)
    }

    fun addData(newItem: SpecialTopicsDetailResponse) {
        val currentList = _dataList.value?.toMutableList() ?: mutableListOf()
        currentList.add(newItem)
        _dataList.value = currentList
    }

    fun clearData() {
        _dataList.value = emptyList()
    }
}