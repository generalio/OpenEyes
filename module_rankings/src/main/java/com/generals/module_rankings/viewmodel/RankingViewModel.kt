import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.generals.module_rankings.model.bean.Item
import com.generals.module_rankings.model.bean.NavigationEvent
import com.generals.module_rankings.model.bean.VideoType
import com.generals.module_rankings.model.net.RankingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RankingViewModel : ViewModel() {
    private var hasDemotedFeatured = false

    private val repository = RankingRepository()

    // 原有的UI状态管理...
    private val _uiState = MutableStateFlow(RankingUiState())
    // 原有的排行数据...
    private val _monthlyRanking = MutableStateFlow<List<Item>>(emptyList())
    val monthlyRanking: StateFlow<List<Item>> = _monthlyRanking.asStateFlow()

    private val _totalRanking = MutableStateFlow<List<Item>>(emptyList())
    val totalRanking: StateFlow<List<Item>> = _totalRanking.asStateFlow()

    // ==== 新增：视频滚动相关状态 ====

    // 视频列表数据
    private val _videoList = MutableStateFlow<List<Any>>(emptyList())
    val videoList: StateFlow<List<Any>> = _videoList.asStateFlow()

    // 视频模式状态
    private val _isVideoMode = MutableStateFlow(true)
    val isVideoMode: StateFlow<Boolean> = _isVideoMode.asStateFlow()

    // 导航事件
    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent.asStateFlow()

    // 私有状态
    private var featuredVideoItem: Item? = null
    private var contentItems: List<Item> = emptyList()

    // ==== 原有方法保持不变 ====

    fun getMonthlyRanking() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository.getRanking("monthly").fold(
                onSuccess = { response ->
                    _monthlyRanking.value = response.itemList
                    _uiState.value = _uiState.value.copy(isLoading = false)
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "获取月排行数据失败"
                    )
                }
            )
        }
    }

    fun getTotalRanking() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository.getRanking("historical").fold(
                onSuccess = { response ->
                    _totalRanking.value = response.itemList
                    _uiState.value = _uiState.value.copy(isLoading = false)
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "获取总排行数据失败"
                    )
                }
            )
        }
    }

    // ==== 新增：视频滚动相关方法 ====

    /**
     * 加载视频列表数据
     */
    fun loadVideoList(strategy: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository.getRanking(strategy).fold(
                onSuccess = { response ->
                    val items = response.itemList
                    if (items.isNotEmpty()) {
                        featuredVideoItem = items.first()
                        contentItems = items.drop(1)
                        updateVideoUiList()
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false)
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "加载视频数据失败"
                    )
                }
            )
        }
    }

    /**
     * 更新视频可见性
     */
    fun updateVideoVisibility(isVisible: Boolean) {
        if (_isVideoMode.value != isVisible) {
            _isVideoMode.value = isVisible
        }
    }

    /**
     * 处理项目点击
     */
    fun onItemClick(item: Any) {
        when (item) {
            is Item -> {
                if (item == featuredVideoItem && _isVideoMode.value == false) {
                    // 点击转换后的视频，回到视频模式
                    _isVideoMode.value = true
                    updateVideoUiList()
                } else {
                    // 其他内容项，跳转详情
                    _navigationEvent.value = NavigationEvent.ToVideoDetail(item)
                }
            }
        }
    }

    /**
     * 获取特色视频
     */
    fun getFeaturedVideo(): Item? = featuredVideoItem
    /**
     * 清除导航事件
     */
    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }
    /**
     * 更新视频UI列表
     */
    private fun updateVideoUiList() {
        val list = mutableListOf<Any>()

        //  只在没被降级过，且视频模式下，且数据不为空，才添加视频播放器
        if (!hasDemotedFeatured && _isVideoMode.value && featuredVideoItem != null) {
            list.add(VideoType(featuredVideoItem!!))
        }

        list.addAll(contentItems)
        _videoList.value = list
    }



    fun demoteFeaturedVideoToNormalItem() {
        featuredVideoItem?.let {
            if (!contentItems.contains(it)) {
                contentItems = listOf(it) + contentItems
            }
        }

        featuredVideoItem = null
        hasDemotedFeatured = true //
        updateVideoUiList()
    }
    data class RankingUiState(
        val isLoading: Boolean = false,
        val error: String? = null
    )
}