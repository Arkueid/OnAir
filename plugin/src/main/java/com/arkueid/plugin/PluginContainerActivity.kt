package com.arkueid.plugin

import android.app.Activity
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.Toast

/**
 * @author: Arkueid
 * @date: 2024/9/6
 * @desc:
 */
class PluginContainerActivity : Activity() {
    private lateinit var pluginLoader: PluginLoader

    companion object {
        private const val TAG = "PluginContainerActivity"
    }

    init {
        pluginLoader = PluginLoaderManager.currentLoader!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        intent.getStringExtra("className")?.let { className ->
            createPlugin(className, savedInstanceState)
        }

        if (!this::pluginLoader.isInitialized) {
            Toast.makeText(this, "插件页面加载失败", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createPlugin(className: String, savedInstanceState: Bundle?) {
        val clazz = classLoader.loadClass(className)
        val constructor = clazz.getConstructor()
        val instance: IPlugin = constructor.newInstance() as IPlugin
        instance.attach(this)
        instance.onCreate(savedInstanceState)
    }

    override fun getResources(): Resources {
        return pluginLoader.resources
    }

    override fun getClassLoader(): ClassLoader {
        return pluginLoader.classLoader
    }

}