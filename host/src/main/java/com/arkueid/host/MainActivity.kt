package com.arkueid.host

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.arkueid.plugin.PluginContainerActivity
import com.arkueid.plugin.PluginLoader
import java.io.File

class MainActivity : AppCompatActivity(), OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)


        val loadBtn = findViewById<Button>(R.id.loadBtn)
        val gotoBtn = findViewById<Button>(R.id.gotoBtn)
        loadBtn.setOnClickListener(this)
        gotoBtn.setOnClickListener(this)
    }

    private fun load() {
        val apk = File(Environment.getExternalStorageDirectory(), "testsource-debug.apk")
        PluginLoader.load(apk.absolutePath)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.loadBtn -> load()
            R.id.gotoBtn -> goto()
        }
    }

    private fun goto() {
        startActivity(Intent(this, PluginContainerActivity::class.java))
    }
}