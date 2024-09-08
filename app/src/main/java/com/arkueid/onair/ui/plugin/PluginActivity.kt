package com.arkueid.onair.ui.plugin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.arkueid.onair.SourceViewModel
import com.arkueid.onair.databinding.ActivityPluginBinding
import com.arkueid.onair.utils.ToastUtils
import com.arkueid.plugin.PluginLoaderManager
import com.arkueid.plugin.startActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PluginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ExtensionActivity"

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
        pluginAdapter = PluginAdapter(sourceViewModel.plugins) { item ->
            item.startActivity(this@PluginActivity, item.manifest.settingsActivityId!!)
        }
        binding.recyclerView.adapter = pluginAdapter

    }


}