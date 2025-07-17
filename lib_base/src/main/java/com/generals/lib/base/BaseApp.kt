package com.generals.lib.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.github.anrwatchdog.ANRWatchDog

/**
 * @Desc : BaseApp
 * @Author : zzx
 * @Date : 2025/7/13 10:30
 */

class BaseApp : Application() {

    private val isDebug = true

    override fun onCreate() {
        super.onCreate()

        val anrWatchDog = ANRWatchDog()
        anrWatchDog.start()

        if(isDebug) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }

}