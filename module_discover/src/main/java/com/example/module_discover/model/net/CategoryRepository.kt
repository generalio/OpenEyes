package com.example.module_discover.model.net
import com.example.module_discover.model.bean.CategoryResponse
import com.example.module_discover.model.bean.SpecialTopicsDetailResponse
import com.example.module_discover.model.bean.SpecialTopicsResponse

class CategoryRepository {
    private val categoryService: CategoryService=RetrofitClient.retrofit
    suspend fun getCategory(): Result<CategoryResponse> {
        return try {
            val response = categoryService.getCategory()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e) // 失败返回异常
        }
    }
    suspend fun getSpecialTopics():Result<SpecialTopicsResponse>{
        return try {
            val response=categoryService.getSpecialTopics()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e) // 失败返回异常
        }
    }
    suspend fun getSpecialTopicsDetail(SpecialTopicsId:Int):
            Result<SpecialTopicsDetailResponse>{
        return try {
            val response=categoryService.getSpecialTopicsDetail(SpecialTopicsId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e) // 失败返回异常
        }
    }

}
