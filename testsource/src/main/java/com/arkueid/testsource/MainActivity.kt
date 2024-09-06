package com.arkueid.testsource

import android.os.Bundle
import com.arkueid.plugin.PluginContext

class MainActivity : PluginContext() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        host.setContentView(R.layout.activity_main)
    }

}