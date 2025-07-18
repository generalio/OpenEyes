package com.example.module_discover.model.net
import CategorySource
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.module_discover.model.bean.CategoryDetailItem
import com.example.module_discover.model.bean.CategoryDetailResponse
import com.example.module_discover.model.bean.CategoryResponse
import com.example.module_discover.model.bean.SpecialTopicsDetailResponse
import com.example.module_discover.model.bean.SpecialTopicsResponse
import kotlinx.coroutines.flow.Flow

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
    suspend fun getCategoryDetailResponse(CategoryId:Int,start:String,num:String,udid:String):
            Result<CategoryDetailResponse>{
        return try {
            val response=categoryService.getCategoryDetail(CategoryId,start,num,udid)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e) // 失败返回异常
        }
    }
    fun getCategoryDetail(id:Int): Flow<PagingData<CategoryDetailItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                CategorySource(
                    id=id,
                    categoryService=categoryService
                )
            }
        ).flow
    }

}
