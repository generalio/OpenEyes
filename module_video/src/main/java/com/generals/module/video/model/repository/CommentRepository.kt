package com.generals.module.video.model.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.generals.lib.net.ServiceCreator
import com.generals.module.video.model.net.HotCommentService
import com.generals.module.video.model.net.NewCommentPagingSource
import com.generals.module.video.model.net.NewCommentService

/**
 * @Desc : 类的描述
 * @Author : zzx
 * @Date : 2025/7/16 14:45
 */

object CommentRepository {

    private val hotCommentService = ServiceCreator.create<HotCommentService>()
    private val newCommentService = ServiceCreator.create<NewCommentService>()

    fun getHotComment(videoId: Int) = hotCommentService.getHotCommentService(videoId)
    fun getNewComment(videoId: Int) = Pager(config = PagingConfig(10), pagingSourceFactory = {
        NewCommentPagingSource(newCommentService, videoId) }
    )

}