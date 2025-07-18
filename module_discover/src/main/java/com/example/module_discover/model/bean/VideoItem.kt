package com.example.module_discover.model.bean

// VideoItem 作为 ThemeDetailItem 的子类
data class VideoItem(
    val icon: String,
    val iconName: String,
    val time: String,
    val title: String,
    val subTitle: String,
    val tag1: String,
    val tag2: String,
    val tag3: String,
    val videoImage: String
) : ThemeDetailItem()
