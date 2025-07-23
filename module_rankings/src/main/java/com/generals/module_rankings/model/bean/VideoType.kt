package com.generals.module_rankings.model.bean

data class VideoType(val item: Item)

// 导航事件
sealed class NavigationEvent {
    data class ToVideoDetail(val item: Item) : NavigationEvent()
}



