package com.generals.module.video.model.net

import android.net.Uri
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.generals.module.video.model.bean.Comment

/**
 * @Desc : 最新评论的PagingSource
 * @Author : zzx
 * @Date : 2025/7/16 16:51
 */

class NewCommentPagingSource(private val newCommentService: NewCommentService, private val videoId: Int) : PagingSource<String, Comment>() {
    override fun getRefreshKey(state: PagingState<String, Comment>): String? {
        return null
    }

    /**
     * 不仅需要对nextPageUrl进行提取还需要检测这个接口的item
     * 如果有最新评论的字样则将最新评论以下的加入itemList并返回
     * 否则就全为最新评论，直接返回即可
     */
    override suspend fun load(params: LoadParams<String>): LoadResult<String, Comment> {
        return try {
            val pageUrl = params.key // 拿到pageUrl
            val num = pageUrl?.let {
                Uri.parse(pageUrl).getQueryParameter("num")
            }
            val lastId = pageUrl?.let {
                Uri.parse(pageUrl).getQueryParameter("lastId")
            }
            val commentResponse = newCommentService.getNewComment(lastId, videoId, num)
            val itemList = commentResponse.itemList
            val commentList: MutableList<Comment> = mutableListOf()
            var mark = true
            for(comment in itemList) {
                if(comment.type != "reply") {
                    mark = false
                }
            }
            if(mark) {
                LoadResult.Page(itemList, null, commentResponse.nextPageUrl)
            } else {
                var isNew = false
                for(comment in itemList) {
                    if(comment.type == "leftAlignTextHeader" && comment.data.text == "最新评论") {
                        isNew = true
                        continue
                    }
                    if(comment.type != "reply") {
                        continue
                    }
                    if(isNew) {
                        commentList.add(comment)
                    }
                }
                LoadResult.Page(commentList.toList(), null, commentResponse.nextPageUrl)
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}