package com.generals.lib.base

import android.app.Application
import com.alibaba.android.arouter.BuildConfig
import com.alibaba.android.arouter.launcher.ARouter
/**
 * @Desc : BaseApp
 * @Author : zzx
 * @Date : 2025/7/13 10:30
 */

class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }

}