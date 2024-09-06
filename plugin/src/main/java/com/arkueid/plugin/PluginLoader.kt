package com.arkueid.plugin

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.res.Resources
import dalvik.system.DexClassLoader

/**
 * @author: Arkueid
 * @date: 2024/9/6
 * @desc:
 */
class PluginLoader(private val proxyContext: Context) {

    private lateinit var _resources: Resources
    val resources: Resources get() = _resources
    private lateinit var _dexClassLoader: DexClassLoader
    val classLoader: DexClassLoader get() = _dexClassLoader

    fun load(pluginPath: String) {
        // dex
        val optimizedDirectory = proxyContext.getDir("dex", Context.MODE_PRIVATE).absolutePath
        _dexClassLoader =
            DexClassLoader(pluginPath, optimizedDirectory, null, proxyContext.classLoader)
        val applicationInfo = ApplicationInfo()

        // resources
        applicationInfo.packageName = proxyContext.applicationInfo.packageName
        applicationInfo.uid = proxyContext.applicationInfo.uid
        applicationInfo.publicSourceDir = pluginPath
        applicationInfo.sourceDir = pluginPath
        applicationInfo.sharedLibraryFiles = proxyContext.applicationInfo.sharedLibraryFiles
        _resources = proxyContext.packageManager.getResourcesForApplication(applicationInfo)
    }
}