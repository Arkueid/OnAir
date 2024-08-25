package com.arkueid.onair.ui

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.arkueid.onair.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    // milliseconds
    companion object {
        private const val DISPLAY_DURATION = 500L
    }


    private val runnable: Runnable = Runnable {
        val intent = Intent(applicationContext, MainActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(
            this,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        ).toBundle()
        startActivity(intent, options)
        finish()
    }

    private val handler: Handler by lazy { Handler(mainLooper) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, DISPLAY_DURATION)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}