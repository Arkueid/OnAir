package com.arkueid.onair.ui.splash

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.arkueid.onair.R
import com.arkueid.onair.ui.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    // milliseconds
    companion object {
        private const val DISPLAY_DURATION = 200L
        private const val TAG = "SplashActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        goToMain()
    }

    private fun goToMain() {
        lifecycleScope.launch {
            delay(DISPLAY_DURATION)
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            val options = ActivityOptions.makeCustomAnimation(
                this@SplashActivity,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            ).toBundle()
            startActivity(intent, options)
            finish()
        }
    }
}