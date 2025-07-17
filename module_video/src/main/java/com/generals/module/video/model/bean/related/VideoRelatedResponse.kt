package com.generals.module.video.model.bean.related

/**
 * @Desc : 视频推荐的数据类
 * @Author : zzx
 * @Date : 2025/7/17 14:15
 */

data class VideoRelatedResponse(
    val adExist: Boolean,
    val count: Int,
    val itemList: List<VideoRelated>,
    val nextPageUrl: Any,
    val total: Int
)

data class VideoRelated(
    val `data`: Data,
    val id: Int,
    val type: String
)

data class Data(
    val actionUrl: String,
    val ad: Boolean,
    val author: Author,
    val category: String,
    val collected: Boolean,
    val consumption: Consumption,
    val cover: Cover,
    val dataType: String,
    val date: Long,
    val description: String,
    val descriptionEditor: String,
    val descriptionPgc: String,
    val duration: Int,
    val id: Int,
    val idx: Int,
    val ifLimitVideo: Boolean,
    val library: String,
    val playInfo: List<PlayInfo>,
    val playUrl: String,
    val played: Boolean,
    val provider: Provider,
    val reallyCollected: Boolean,
    val releaseTime: Long,
    val remark: String,
    val resourceType: String,
    val searchWeight: Int,
    val slogan: String,
    val tags: List<Tag>,
    val text: String,
    val thumbPlayUrl: String,
    val title: String,
    val titlePgc: String,
    val type: String,
    val videoPosterBean: VideoPosterBean,
    val webUrl: WebUrl
)

data class Author(
    val approvedNotReadyVideoCount: Int,
    val description: String,
    val expert: Boolean,
    val follow: Follow,
    val icon: String,
    val id: Int,
    val ifPgc: Boolean,
    val latestReleaseTime: Long,
    val link: String,
    val name: String,
    val recSort: Int,
    val shield: Shield,
    val videoNum: Int
)

data class Consumption(
    val collectionCount: Int,
    val realCollectionCount: Int,
    val replyCount: Int,
    val shareCount: Int
)

data class Cover(
    val blurred: String,
    val detail: String,
    val feed: String,
    val homepage: String,
)

data class PlayInfo(
    val height: Int,
    val name: String,
    val type: String,
    val url: String,
    val urlList: List<Url>,
    val width: Int
)

data class Provider(
    val alias: String,
    val icon: String,
    val name: String
)

data class Tag(
    val actionUrl: String,
    val bgPicture: String,
    val communityIndex: Int,
    val desc: String,
    val haveReward: Boolean,
    val headerImage: String,
    val id: Int,
    val ifNewest: Boolean,
    val name: String,
    val tagRecType: String
)

data class VideoPosterBean(
    val fileSizeStr: String,
    val scale: Double,
    val url: String
)

data class WebUrl(
    val forWeibo: String,
    val raw: String
)

data class Follow(
    val followed: Boolean,
    val itemId: Int,
    val itemType: String
)

data class Shield(
    val itemId: Int,
    val itemType: String,
    val shielded: Boolean
)

data class Url(
    val name: String,
    val size: Int,
    val url: String
)