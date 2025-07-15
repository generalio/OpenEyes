package com.generals.module.video.model.bean

/**
 * @Desc : 视频信息的类
 * @Author : zzx
 * @Date : 2025/7/15 16:10
 */

data class VideoInfo(
    val id: Int,
    val title: String,
    val subTitle: String,
    val description: String,
    val collectionCount: Int,
    val shareCount: Int,
    val replyCount: Int,
    val background: String,
    val playUrl: String,
    val authorName: String,
    val authorIcon: String,
    val authorDescription: String,
    val cover: String
)
