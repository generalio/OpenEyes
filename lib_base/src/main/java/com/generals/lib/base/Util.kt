package com.generals.lib.base

/**
 * @Desc : 一些工具类
 * @Author : zzx
 * @Date : 2025/7/14 11:50
 */

object Util {

    // api提供的duration是秒的形式，转换成00:00
    fun transferTime(duration: Int) : String {
        val resultBuilder = StringBuilder()
        val minute = duration / 60
        val second = duration % 60
        if(minute < 10)resultBuilder.append("0")
        resultBuilder.append(minute)
        resultBuilder.append(":")
        if(second < 10)resultBuilder.append("0")
        resultBuilder.append(second)
        return resultBuilder.toString()
    }

    fun convertImgUrl(url: String): String {
        return url.replaceFirst("img.kaiyanapp.com", "ali-img.kaiyanapp.com")
    }

}