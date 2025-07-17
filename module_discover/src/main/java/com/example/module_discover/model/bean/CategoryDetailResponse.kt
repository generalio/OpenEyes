package com.example.module_discover.model.bean

data class CategoryDetailResponse(
    val adExist: Boolean,
    val count: Int,
    val itemList: List<CategoryDetailItem>,
    val nextPageUrl: String,
    val total: Int
)

data class CategoryDetailItem(
    val adIndex: Int,
    val `data`: CategoryDetailData,
    val id: Int,
    val tag: Any,
    val trackingData: Any,
    val type: String
)

data class CategoryDetailData(
    val actionUrl: String,
    val ad: Boolean,
    val adTrack: List<Any?>,
    val author: CategoryDetailAuthor,
    val brandWebsiteInfo: Any,
    val campaign: Any,
    val category: String,
    val collected: Boolean,
    val consumption: CategoryDetailConsumption,
    val content: CategoryDetailContent,
    val cover: CoverX,
    val dataType: String,
    val date: Long,
    val description: String,
    val descriptionEditor: String,
    val descriptionPgc: String,
    val duration: Int,
    val favoriteAdTrack: Any,
    val follow: Any,
    val header: CategoryDetailHeader,
    val id: Int,
    val idx: Int,
    val ifLimitVideo: Boolean,
    val label: Any,
    val labelList: List<Any?>,
    val lastViewTime: Any,
    val library: String,
    val playInfo: List<PlayInfoX>,
    val playUrl: String,
    val played: Boolean,
    val playlists: Any,
    val promotion: Any,
    val provider: ProviderX,
    val reallyCollected: Boolean,
    val recallSource: String,
    val recall_source: String,
    val releaseTime: Long,
    val remark: String,
    val resourceType: String,
    val searchWeight: Int,
    val shareAdTrack: Any,
    val slogan: String,
    val src: Int,
    val subTitle: Any,
    val subtitles: List<Any?>,
    val tags: List<TagX>,
    val text: String,
    val thumbPlayUrl: String,
    val title: String,
    val titlePgc: String,
    val type: String,
    val videoPosterBean: VideoPosterBeanX,
    val waterMarks: Any,
    val webAdTrack: Any,
    val webUrl: WebUrlX
)

data class CategoryDetailAuthor(
    val adTrack: Any,
    val approvedNotReadyVideoCount: Int,
    val description: String,
    val expert: Boolean,
    val follow: CategoryDetailFollow,
    val icon: String,
    val id: Int,
    val ifPgc: Boolean,
    val latestReleaseTime: Long,
    val link: String,
    val name: String,
    val recSort: Int,
    val shield: CategoryDetailShield,
    val videoNum: Int
)

data class CategoryDetailConsumption(
    val collectionCount: Int,
    val realCollectionCount: Int,
    val replyCount: Int,
    val shareCount: Int
)

data class CategoryDetailContent(
    val adIndex: Int,
    val `data`: CategoryDetailDataX,
    val id: Int,
    val tag: Any,
    val trackingData: Any,
    val type: String
)

data class CoverX(
    val blurred: String,
    val detail: String,
    val feed: String,
    val homepage: String,
    val sharing: Any
)

data class CategoryDetailHeader(
    val actionUrl: String,
    val cover: Any,
    val description: String,
    val font: Any,
    val icon: String,
    val iconType: String,
    val id: Int,
    val label: Any,
    val labelList: Any,
    val rightText: Any,
    val showHateVideo: Boolean,
    val subTitle: Any,
    val subTitleFont: Any,
    val textAlign: String,
    val time: Long,
    val title: String
)

data class PlayInfoX(
    val height: Int,
    val name: String,
    val type: String,
    val url: String,
    val urlList: List<CategoryDetailUrl>,
    val width: Int
)

data class ProviderX(
    val alias: String,
    val icon: String,
    val name: String
)

data class TagX(
    val actionUrl: String,
    val adTrack: Any,
    val bgPicture: String,
    val childTagIdList: Any,
    val childTagList: Any,
    val communityIndex: Int,
    val desc: String,
    val haveReward: Boolean,
    val headerImage: String,
    val id: Int,
    val ifNewest: Boolean,
    val name: String,
    val newestEndTime: Any,
    val tagRecType: String
)

data class VideoPosterBeanX(
    val fileSizeStr: String,
    val scale: Double,
    val url: String
)

data class WebUrlX(
    val forWeibo: String,
    val raw: String
)

data class CategoryDetailFollow(
    val followed: Boolean,
    val itemId: Int,
    val itemType: String
)

data class CategoryDetailShield(
    val itemId: Int,
    val itemType: String,
    val shielded: Boolean
)

data class CategoryDetailDataX(
    val ad: Boolean,
    val adTrack: List<Any?>,
    val author: Author,
    val brandWebsiteInfo: Any,
    val campaign: Any,
    val category: String,
    val collected: Boolean,
    val consumption: Consumption,
    val cover: CoverX,
    val dataType: String,
    val date: Long,
    val description: String,
    val descriptionEditor: String,
    val descriptionPgc: String,
    val duration: Int,
    val favoriteAdTrack: Any,
    val id: Int,
    val idx: Int,
    val ifLimitVideo: Boolean,
    val label: Any,
    val labelList: List<Any?>,
    val lastViewTime: Any,
    val library: String,
    val playInfo: List<PlayInfoX>,
    val playUrl: String,
    val played: Boolean,
    val playlists: Any,
    val promotion: Any,
    val provider: ProviderX,
    val reallyCollected: Boolean,
    val recallSource: String,
    val recall_source: String,
    val releaseTime: Long,
    val remark: String,
    val resourceType: String,
    val searchWeight: Int,
    val shareAdTrack: Any,
    val slogan: String,
    val src: Int,
    val subtitles: List<Any?>,
    val tags: List<TagX>,
    val thumbPlayUrl: String,
    val title: String,
    val titlePgc: String,
    val type: String,
    val videoPosterBean: VideoPosterBeanX,
    val waterMarks: Any,
    val webAdTrack: Any,
    val webUrl: WebUrlX
)

data class CategoryDetailUrl(
    val name: String,
    val size: Int,
    val url: String
)