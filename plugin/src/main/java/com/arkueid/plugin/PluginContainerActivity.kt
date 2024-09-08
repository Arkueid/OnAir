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
    private var pluginLoader = PluginLoaderManager.currentLoader

    companion object {
        private const val TAG = "PluginContainerActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.getStringExtra("className")?.let { className ->
            if (pluginLoader == null) {
                Toast.makeText(this, "插件页面加载失败", Toast.LENGTH_SHORT).show()
            } else {
                createPlugin(className, savedInstanceState)
            }
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
        return pluginLoader?.resources ?: super.getResources()
    }

    override fun getClassLoader(): ClassLoader {
        return pluginLoader?.classLoader ?: super.getClassLoader()
    }

    override fun onDestroy() {
        super.onDestroy()
        PluginLoaderManager.pop()
    }

}