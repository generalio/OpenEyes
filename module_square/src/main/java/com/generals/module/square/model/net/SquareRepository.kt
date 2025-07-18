package com.generals.module.square.model.net

import com.generals.lib.net.ServiceCreator

/**
 * @Desc : 广场页的仓库层
 * @Author : zzx
 * @Date : 2025/7/17 21:38
 */

object SquareRepository {

    val squareService = ServiceCreator.create<SquareService>()

    fun getSquareBannerInfo() = squareService.getSquareBannerInfo()
    fun getSquareInfo(startScore: Int, pageCount: Int) = squareService.getSquareInfo(startScore, pageCount)

}