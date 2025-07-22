package com.generals.lib.base

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
/**
 * @Desc : BaseApp
 * @Author : zzx
 * @Date : 2025/7/13 10:30
 */

class BaseApp : Application() {

    private val isDebug = true

    override fun onCreate() {
        super.onCreate()

        if(isDebug) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }

}