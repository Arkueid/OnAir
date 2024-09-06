package com.arkueid.plugin

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent

/**
 * @author: Arkueid
 * @date: 2024/9/6
 * @desc:
 */
interface IPlugin {
    fun attach(proxyActivity: Activity)
    fun onCreate(savedInstanceState: Bundle?)
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
    fun onDestroy()
    fun onSaveInstanceState(outState: Bundle?)
    fun onTouchEvent(event: MotionEvent)
    fun onBackPressed()
}