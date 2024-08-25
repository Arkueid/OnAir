package com.arkueid.onair

import android.app.Application
import com.arkueid.onair.utils.ToastUtils
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        ToastUtils.init(this)
    }
}