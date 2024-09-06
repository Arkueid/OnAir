package com.arkueid.onair.ui.extension

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.arkueid.onair.databinding.ActivityExtensionBinding
import com.arkueid.onair.extension.ExtensionLoader
import com.arkueid.onair.utils.ToastUtils
import java.io.File

class ExtensionActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ExtensionActivity"
        private const val PERMISSION_REQUEST_CODE = 1

    }

    private lateinit var binding: ActivityExtensionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExtensionBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        ExtensionLoader.load(getExternalFilesDir())
        checkPermissions()
        val extension = File(Environment.getExternalStorageDirectory(), "testsource-debug.apk")
        if (extension.exists()) {
            ExtensionLoader.load(extension.absolutePath)?.let {
                binding.pluginId.text = it.key
                binding.pluginName.text = it.name
                binding.pluginVersion.text = it.version
                binding.pluginIcon.setImageDrawable(it.icon)
            } ?: run {
                ToastUtils.showToast("插件加载失败")
            }
        }


    }

    private fun checkPermissions() {
        val permissions = mutableListOf<String>()
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (permissions.isNotEmpty()) {
            requestPermissions(permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
        }
    }

    private fun goToSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // 权限已授予，继续执行
            } else {
                ToastUtils.showToast("请在设置中开启权限")
                goToSetting()
            }
        }
    }
}