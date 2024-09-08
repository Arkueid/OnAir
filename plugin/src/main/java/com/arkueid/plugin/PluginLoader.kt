package com.arkueid.plugin

import android.content.Context
import android.content.Intent
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
    fun <T> create(className: String, vararg args: Any?): T? {
        return try {
            val clazz = classLoader.loadClass(className)
            val constructor = clazz.getConstructor(*(args.map { it?.javaClass }.toTypedArray()))
            constructor.newInstance(*args) as T
        } catch (e: Exception) {
//            throw e
            null
        }
    }

    val manifest: IPluginManifest by lazy {
        val clazz = classLoader.loadClass("$id.PluginManifest")
        val constructor = clazz.getConstructor()
        constructor.newInstance() as IPluginManifest
    }
}

fun PluginLoader.startActivity(context: Context, className: String) {
    PluginLoaderManager.push(this.id)
    context.startActivity(Intent(context, PluginContainerActivity::class.java).apply {
        putExtra("className", className)
    })
}