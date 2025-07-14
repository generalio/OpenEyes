package com.generals.lib.net

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

/**
 * @Desc : Retrofit构建器
 * @Author : zzx
 * @Date : 2025/7/13 10:43
 */

class ServiceCreator {

    private val BASE_URL = "http://baobab.kaiyanapp.com/api/"

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    // 使用: ServiceCreator.create<...Service>()
    inline fun <reified T> create() : T = retrofit.create(T::class.java)

}