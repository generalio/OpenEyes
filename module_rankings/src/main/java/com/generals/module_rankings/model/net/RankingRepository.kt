package com.generals.module_rankings.model.net

import com.generals.lib.net.ServiceCreator
import com.generals.module_rankings.model.bean.RankingResponse

class RankingRepository {
    suspend fun getRanking(strategy:String):Result<RankingResponse>{
        return try {
            val response=ServiceCreator.create<RankingService>().getRanking(strategy)
            Result.success(response)
        }catch (e: Exception) {
            Result.failure(e) // 失败返回异常
        }
    }
}