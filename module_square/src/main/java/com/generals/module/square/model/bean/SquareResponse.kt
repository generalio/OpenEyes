package com.generals.module.square.model.bean

import android.os.Parcel
import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import androidx.versionedparcelable.VersionedParcelize
import kotlinx.parcelize.Parcelize
import java.io.Serializable

/**
 * @Desc : 广场的数据类
 * @Author : zzx
 * @Date : 2025/7/17 21:10
 */

data class SquareResponse(
    val adExist: Boolean,
    val count: Int,
    val itemList: List<Square>,
    val nextPageUrl: String,
    val total: Int
)

data class Square(
    val adIndex: Int,
    val `data`: Data,
    val id: Int,
    val type: String
)
data class Data(
    val content: Content,
    val count: Int,
    val dataType: String,
    val header: Header,
    val itemList: List<ItemDetail>
)

data class Content(
    val adIndex: Int,
    val `data`: DataX,
    val id: Int,
    val type: String
)

data class Header(
    val actionUrl: String,
    val followType: String,
    val icon: String,
    val iconType: String,
    val id: Int,
    val issuerName: String,
    val showHateVideo: Boolean,
    val tagId: Int,
    val time: Long,
    val topShow: Boolean
)

data class ItemDetail(
    val adIndex: Int,
    val `data`: DataXX,
    val id: Int,
    val type: String
)

data class DataX(
    val addWatermark: Boolean,
    val area: String,
    val checkStatus: String,
    val city: String,
    val collected: Boolean,
    val consumption: Consumption,
    val cover: Cover,
    val createTime: Long,
    val dataType: String,
    val description: String,
    val duration: Int,
    val height: Int,
    val id: Int,
    val ifMock: Boolean,
    val latitude: Double,
    val library: String,
    val longitude: Double,
    val owner: Owner,
    val playUrl: String,
    val playUrlWatermark: String,
    val reallyCollected: Boolean,
    val releaseTime: Long,
    val resourceType: String,
    val source: String,
    val title: String,
    val type: String,
    val uid: Int,
    val updateTime: Long,
    val url: String,
    val urls: List<String>,
    val urlsWithWatermark: List<String>,
    val validateResult: String,
    val validateStatus: String,
    val validateTaskId: String,
    val width: Int
)

data class Consumption(
    val collectionCount: Int,
    val realCollectionCount: Int,
    val replyCount: Int,
    val shareCount: Int
)

data class Cover(
    val detail: String,
    val feed: String,
)

data class Owner(
    val actionUrl: String,
    val avatar: String,
    val birthday: Long,
    val city: String,
    val country: String,
    val cover: String,
    val description: String,
    val expert: Boolean,
    val followed: Boolean,
    val gender: String,
    val ifPgc: Boolean,
    val job: String,
    val library: String,
    val limitVideoOpen: Boolean,
    val nickname: String,
    val registDate: Long,
    val releaseDate: Long,
    val uid: Int,
    val university: String,
    val userType: String
)

data class DataXX(
    val actionUrl: String,
    val autoPlay: Boolean,
    val bgPicture: String,
    val dataType: String,
    val description: String,
    val id: Int,
    val image: String,
    val shade: Boolean,
    val subTitle: String,
    val title: String
)

@Parcelize
data class Photo(
    val id: Int,
    val description: String,
    val collectionCount: Int,
    val nickname: String,
    val avatar: String,
    val createTime: Long,
    val updateTime: Long,
    val urls: List<String>
) : Parcelable