package com.arkueid.plugin

import android.content.res.Resources
import androidx.core.content.res.ResourcesCompat
import dalvik.system.DexClassLoader

/**
 * @author: Arkueid
 * @date: 2024/9/6
 * @desc:
 */
class PluginLoader internal constructor(
    val id: String,
    val name: String,
    val versionName: String,
    private val iconResId: Int,
    val resources: Resources,
    val classLoader: DexClassLoader
) {
    val icon get() = ResourcesCompat.getDrawable(resources, iconResId, null)

    @Suppress("UNCHECKED_CAST")
    fun <T> create(className: String): T? {
        return try {
            val clazz = classLoader.loadClass(className)
            val constructor = clazz.getConstructor()
            constructor.newInstance() as T
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    val manifest: IPluginManifest by lazy {
        val clazz = classLoader.loadClass("$id.PluginManifest")
        val constructor = clazz.getConstructor()
        constructor.newInstance() as IPluginManifest
    }
}