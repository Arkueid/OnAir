package com.arkueid.onair.ui.plugin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.arkueid.onair.SourceViewModel
import com.arkueid.onair.databinding.ActivityPluginBinding
import com.arkueid.onair.utils.ToastUtils
import com.arkueid.plugin.PluginContainerActivity
import com.arkueid.plugin.PluginLoader
import com.arkueid.plugin.PluginLoaderManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class PluginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ExtensionActivity"
        private const val PERMISSION_REQUEST_CODE = 1

    }

    private lateinit var binding: ActivityPluginBinding
    private lateinit var pluginAdapter: PluginAdapter

    @Inject
    lateinit var sourceViewModel: SourceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPluginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        pluginAdapter = PluginAdapter(emptyList(), { sourceId, enable ->
            if (enable) {
                sourceViewModel.register(sourceId)
            } else {
                sourceViewModel.unregister(sourceId)
            }
        }, { item ->
            PluginLoaderManager.push(item.id)
            Intent(this, PluginContainerActivity::class.java).apply {
                putExtra("className", item.manifest.settingsActivityId)
            }.let {
                startActivity(it)
            }
        })
        binding.recyclerView.adapter = pluginAdapter

        checkPermissions()
    }

    @Suppress("NotifyDataSetChanged")
    private fun setupPluginList() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val loaderList = mutableListOf<PluginLoader>()
                withContext(Dispatchers.IO) {
                    val pluginPath = Environment.getExternalStorageDirectory()
                    pluginPath.listFiles()?.let { list ->
                        for (file in list) {
                            if (file.path.endsWith(".apk")) {
                                PluginLoaderManager.create(file.absolutePath)?.let {
                                    loaderList.add(it)
                                }
                            }
                        }
                    }
                }
                pluginAdapter.data = loaderList
                pluginAdapter.notifyDataSetChanged()
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
        } else {
            setupPluginList()
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
                setupPluginList()
            } else {
                ToastUtils.showToast("请在设置中开启权限")
                goToSetting()
            }
        }
    }
}