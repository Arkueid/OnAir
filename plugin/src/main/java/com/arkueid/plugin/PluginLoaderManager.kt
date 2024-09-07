package com.arkueid.plugin

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import dalvik.system.DexClassLoader
import dalvik.system.DexFile
import java.util.Stack

private val pluginMapper = mutableMapOf<String, PluginLoader>()

object PluginLoaderManager {

    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context
    }

    private val pluginIdStack = Stack<String>()

    val currentLoader get() = pluginMapper[pluginIdStack.firstElement()]

    fun push(pluginId: String) {
        pluginIdStack.push(pluginId)
    }

    fun pop() {
        pluginIdStack.pop()
    }

    fun get(pluginId: String): PluginLoader {
        return pluginMapper[pluginId] ?: throw IllegalArgumentException("Plugin not found")
    }

    fun create(pluginPath: String): PluginLoader? {
        val packageInfo = applicationContext.packageManager.getPackageArchiveInfo(
            pluginPath,
            PackageManager.GET_META_DATA
        )

        if (packageInfo == null || !validatePlugin(packageInfo)) return null

        val key = packageInfo.packageName

        synchronized(pluginMapper) {
            if (pluginMapper.containsKey(key)) return pluginMapper[key]

            val dexClassLoader = createDexClassLoader(pluginPath)
            val resources = createResources(pluginPath, packageInfo.applicationInfo)
            val name = packageInfo.applicationInfo.metaData.getString("on_air_ext_name") ?: ""
            val versionName =
                packageInfo.applicationInfo.metaData.getString("on_air_ext_version_name") ?: ""

            val iconResId = packageInfo.applicationInfo.icon

            return PluginLoader(
                key,
                name,
                versionName,
                iconResId,
                resources,
                dexClassLoader
            ).also { pluginMapper[key] = it }
        }
    }

    private fun validatePlugin(packageInfo: PackageInfo): Boolean {
        return packageInfo.applicationInfo?.metaData?.containsKey("on_air_ext") ?: false
    }

    private fun createDexClassLoader(pluginPath: String): DexClassLoader {
        return DexClassLoader(
            pluginPath,
            applicationContext.cacheDir.absolutePath,
            null,
            applicationContext.classLoader
        )
    }

    private fun createResources(
        pluginPath: String,
        pluginApplicationInfo: ApplicationInfo
    ): Resources {
        val applicationInfo = ApplicationInfo()
        applicationInfo.packageName = applicationContext.applicationInfo.packageName
        applicationInfo.uid = applicationContext.applicationInfo.uid
        applicationInfo.sourceDir = applicationContext.applicationInfo.sourceDir
        applicationInfo.publicSourceDir = applicationContext.applicationInfo.publicSourceDir
        val sharedLibs = applicationContext.applicationInfo.sharedLibraryFiles
        applicationInfo.sharedLibraryFiles = if (sharedLibs != null) {
            arrayOf(*sharedLibs, pluginPath)
        } else {
            arrayOf(pluginPath)
        }
        return applicationContext.packageManager.getResourcesForApplication(applicationInfo)
    }

}