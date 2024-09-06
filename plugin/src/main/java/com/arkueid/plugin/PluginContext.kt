package com.arkueid.plugin

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent

/**
 * @author: Arkueid
 * @date: 2024/9/6
 * @desc:
 */
open class PluginContext : IPlugin {
    protected lateinit var host: Activity
    override fun attach(proxyActivity: Activity) {
        host = proxyActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {

    }

    override fun onStart() {

    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onStop() {

    }

    override fun onDestroy() {

    }

    override fun onSaveInstanceState(outState: Bundle?) {

    }

    override fun onTouchEvent(event: MotionEvent) {

    }

    override fun onBackPressed() {

    }
}