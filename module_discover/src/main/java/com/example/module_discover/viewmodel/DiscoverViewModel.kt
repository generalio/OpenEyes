package com.example.module_discover.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.module_discover.model.bean.CategoryResponse
import com.example.module_discover.model.bean.ImageItem
import com.example.module_discover.model.bean.SpecialTopicsDetailResponse
import com.example.module_discover.model.bean.SpecialTopicsResponse
import com.example.module_discover.model.bean.ThemeDetailItem
import com.example.module_discover.model.bean.ThemeItem
import com.example.module_discover.model.bean.TitleItem
import com.example.module_discover.model.bean.VideoItem
import com.example.module_discover.model.net.CategoryRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DiscoverViewModel : ViewModel() {
    private val repository = CategoryRepository()

    // 内部可变的 MutableLiveData，用于收集 ThemeItem
    private val _themeListLiveData = MutableLiveData<List<ThemeItem>>()
    // 对外暴露的 LiveData，供 UI 层观察
    val themeList: LiveData<List<ThemeItem>> = _themeListLiveData
    private val _themeDetailLiveData = MutableLiveData<List<ThemeDetailItem>>()
    val themeDetailList:LiveData<List<ThemeDetailItem>> = _themeDetailLiveData

    // 内部可见的 MutableLiveData
    private val _category = MutableLiveData<CategoryResponse>()
    private val _specialTopics = MutableLiveData<SpecialTopicsResponse>()
    private val _loading = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<String>()

    // 外部可见的 LiveData
    val category: LiveData<CategoryResponse> = _category
    val loading: LiveData<Boolean> = _loading
    val error: LiveData<String> = _error

    fun loadThemeItemData() {
        viewModelScope.launch {
            _loading.value = true
            val themeList = mutableListOf<ThemeItem>() // 使用本地变量
            try {
                // 1. 先获取专题列表（同步获取结果，避免依赖 LiveData 的时机问题）
                val specialTopicsResult = repository.getSpecialTopics()
                specialTopicsResult.onSuccess { specialTopics ->
                    // 2. 遍历专题列表，逐个加载详情
                    for (i in 0 until specialTopics.count) {
                        // 获取当前专题的 id（注意空安全处理）
                        val topicId = specialTopics.itemList.getOrNull(i)?.data?.id ?: continue

                        // 3. 加载该专题的详情
                        val detailResult = repository.getSpecialTopicsDetail(topicId)
                        detailResult.onSuccess { detail ->
                            // 4. 从详情数据中提取字段，转换为 ThemeItem
                            val themeItem = ThemeItem(
                                title = detail.text ?: detail.brief ?: "未知专题",
                                imageUrl = detail.headerImage ?: "",
                                id = detail.id
                            )
                            // 5. 添加到列表
                            themeList.add(themeItem)
                        }.onFailure { e ->
                            Log.e("DiscoverViewModel", "加载专题 $topicId 详情失败", e)
                        }
                    }
                    // 6. 一次性更新 LiveData
                    _themeListLiveData.value = themeList.toList()
                }.onFailure { e ->
                    _error.value = "获取专题列表失败: ${e.message}"
                    Log.e("DiscoverViewModel", "获取专题列表失败", e)
                }
            } catch (e: Exception) {
                _error.value = "加载主题数据异常: ${e.message}"
                Log.e("DiscoverViewModel", "主题数据加载异常", e)
            } finally {
                _loading.value = false
            }
        }
    }


    // 加载所有数据
    fun loadAllData() {
        viewModelScope.launch {
            _loading.value = true
            try {
                // 并发加载分类和专题数据
                val categoryDeferred = async { repository.getCategory() }
                val specialTopicsDeferred = async { repository.getSpecialTopics() }

                // 等待两个请求完成
                val categoryResult = categoryDeferred.await()
                val specialTopicsResult = specialTopicsDeferred.await()

                // 处理分类数据结果
                categoryResult.onSuccess { categoryData ->
                    _category.value = categoryData
                    Log.d("DiscoverViewModel", "分类数据加载成功")
                }.onFailure { exception ->
                    _error.value = "加载分类数据失败: ${exception.message}"
                    Log.e("DiscoverViewModel", "分类数据加载失败", exception)
                }

                // 处理专题数据结果
                specialTopicsResult.onSuccess { topicsData ->
                    _specialTopics.value = topicsData
                    Log.d("DiscoverViewModel", "专题数据加载成功")
                }.onFailure { exception ->
                    _error.value = "加载专题数据失败: ${exception.message}"
                    Log.e("DiscoverViewModel", "专题数据加载失败", exception)
                }

            } catch (e: Exception) {
                _error.value = "加载数据异常: ${e.message}"
                Log.e("DiscoverViewModel", "加载数据异常", e)
            } finally {
                _loading.value = false
            }
        }
    }

    // 刷新数据
    fun refreshData() {
        loadAllData()
    }
}