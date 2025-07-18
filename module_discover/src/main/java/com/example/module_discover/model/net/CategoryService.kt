package com.example.module_discover.model.net


import com.example.module_discover.model.bean.CategoryDetailResponse
import com.example.module_discover.model.bean.CategoryResponse
import com.example.module_discover.model.bean.SpecialTopicsDetailResponse
import com.example.module_discover.model.bean.SpecialTopicsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface CategoryService {
    @GET("v5/index/tab/list")
    suspend fun getCategory():CategoryResponse
    @GET("v3/specialTopics")
    suspend fun getSpecialTopics():SpecialTopicsResponse
    @GET("v3/lightTopics/internal/{SpecialTopicsId}")
    suspend fun getSpecialTopicsDetail(
        @Path("SpecialTopicsId") SpecialTopicsId:Int
    ):SpecialTopicsDetailResponse

    @GET("v5/index/tab/category/{categoryId}")
    suspend fun getCategoryDetail(
        @Path("categoryId") categoryId:Int,
        @Query("start") start:String,
        @Query("num") num:String,
        @Query("udid") udid:String
    ):CategoryDetailResponse
    @GET
    suspend fun getCategoryByUrl(@Url url: String): CategoryDetailResponse
}