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

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    private val isDebug = true

    override fun onCreate() {
        super.onCreate()

        context = applicationContext

        val anrWatchDog = ANRWatchDog()
        anrWatchDog.start()
        anrWatchDog.setANRListener {
            Log.e("ANR-WatchDog", "ANR detected!", it);
        }

        if(isDebug) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }

}