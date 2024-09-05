package com.arkueid.onair

import android.app.Application
import com.arkueid.onair.extension.ExtensionLoader
import com.arkueid.onair.utils.ToastUtils
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        ToastUtils.init(this)

        MMKV.initialize(this)

        ExtensionLoader.init(this)

    }
}